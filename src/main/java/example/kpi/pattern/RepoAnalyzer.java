package example.kpi.pattern;

import example.kpi.model.result.*;
import example.kpi.pattern.checkers.line.LineChecker;
import example.kpi.pattern.checkers.line.generic.RegexLineChecker;
import example.kpi.pattern.file.FileAnalyzer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
public class RepoAnalyzer {
    private final String repoName;
    private final RepoContent repoContent;
    private final AppConfiguration configuration;

    public RepoAnalysisResult analyze() {
        Collection<File> repoFilesToBeAnalyzed = FileUtils.listFiles(
                repoContent.getRepoDirectory().toFile(),
                this.configuration.getFileExtensionsToBeAnalyzed().toArray(new String[0]),
                true
        );

        log.debug(() -> String.format(
                "Start analyzing repo %s. Got files to be checked: %s",
                repoName,
                repoFilesToBeAnalyzed)
        );

        final FileAnalyzer fileAnalyzer = new FileAnalyzer();

        final List<RepoIssue> allIssues = repoFilesToBeAnalyzed
                .stream()
                .map(fileAnalyzer::processFile)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new RepoAnalysisResult(this.repoName, allIssues);
    }
}
