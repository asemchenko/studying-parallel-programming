package example.kpi;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.kpi.model.PathCheckerModel;
import example.kpi.model.result.Issue;
import example.kpi.model.result.RepoIssue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
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
        ObjectMapper mapper = new ObjectMapper();

        try {

            final List<PathCheckerModel> result = Arrays.asList(
                    mapper.readValue(
                            PathAnalyzer.class.getResource("/pathCheckers.json"),
                            PathCheckerModel[].class
                    )
            );
            log.info(() -> String.format("Successfully parsed %d path checkers", result.size()));
            return result;
        } catch (IOException e) {
            log.error(() -> "Error during parsing path checkers from file. Skipping...");
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

    public List<RepoIssue> processFile(String path) {
        return PathAnalyzer.checkers
                .stream()
                .map(checker -> checker.check(path))
                .filter(Objects::nonNull)
                .map(issue -> withFullInfo(issue, path))
                .collect(Collectors.toList());
    }

    private RepoIssue withFullInfo(Issue issue, String path) {
        return new RepoIssue(issue, path, null);
    }
}

@Log4j2
@RequiredArgsConstructor
@Getter
class PathChecker {
    private final PathCheckerModel model;
    private final Pattern pattern;

    public Issue check(String path) {
        return hasIssue(path) ? model.getIssue() : null;
    }

    private boolean hasIssue(String path) {
        switch (model.getType()) {
            case PATH:
                return checkRegex(path);
            case FILENAME:
                return checkRegex(FilenameUtils.getName(path));
            case EXTENSION:
                return checkRegex(FilenameUtils.getExtension(path));
            default:
                log.warn(() -> String.format("Unexpected path checker type: %s. Skipping", model.getType()));
                return false;
        }
    }

    private boolean checkRegex(String testString) {
        return pattern.matcher(testString).matches();
    }
}
