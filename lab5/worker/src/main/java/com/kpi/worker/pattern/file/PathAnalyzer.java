package com.kpi.worker.pattern.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpi.worker.di.Provider;
import com.kpi.worker.model.checker.PathCheckerModel;
import com.kpi.worker.model.result.Issue;
import com.kpi.worker.model.result.RepoIssue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Log4j2
public class PathAnalyzer {
    private static final List<PathChecker> checkers = loadCheckers()
            .stream()
            .map(PathAnalyzer::compilePattern)
            .collect(Collectors.toList());

    private static List<PathCheckerModel> loadCheckers() {
        Path pathCheckerDescriptor = Provider.appConfiguration().getPathCheckerDescriptor();
        ObjectMapper mapper = new ObjectMapper();

        try {

            final var result = Arrays.asList(
                    mapper.readValue(
                            pathCheckerDescriptor.toFile(),
                            PathCheckerModel[].class
                    )
            );
            log.info(() -> String.format("Successfully parsed %d path checkers", result.size()));
            return result;
        } catch (IOException e) {
            log.error(() -> String.format("Error during parsing path checkers from file: %s. Skipping...", pathCheckerDescriptor));
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static PathChecker compilePattern(PathCheckerModel model) {
        return new PathChecker(
                model,
                Pattern.compile(model.getPattern())
        );
    }

    public List<RepoIssue> processFile(File fileToBeAnalyzed) {
        return PathAnalyzer.checkers
                .stream()
                .map(checker -> checker.check(fileToBeAnalyzed))
                .filter(Objects::nonNull)
                .map(issue -> withFullInfo(issue, fileToBeAnalyzed))
                .collect(Collectors.toList());
    }

    private RepoIssue withFullInfo(Issue issue, File file) {
        return new RepoIssue(issue, file.getName(), file.getPath(), null);
    }
}

@Log4j2
@RequiredArgsConstructor
@Getter
class PathChecker {
    private final PathCheckerModel model;
    private final Pattern pattern;

    public Issue check(File file) {
        return hasIssue(file) ? model.getIssue() : null;
    }

    private boolean hasIssue(File file) {
        switch (model.getType()) {
            case PATH:
                return checkRegex(file.getPath());
            case FILENAME:
                return checkRegex(file.getName());
            case EXTENSION:
                return checkRegex(FilenameUtils.getExtension(file.getName()));
            default:
                log.warn(() -> String.format("Unexpected path checker type: %s. Skipping", model.getType()));
                return false;
        }
    }

    private boolean checkRegex(String testString) {
        return pattern.matcher(testString).matches();
    }
}
