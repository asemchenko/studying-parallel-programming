package example.kpi.pattern;

import example.kpi.model.result.*;
import example.kpi.pattern.checkers.IssueChecker;
import example.kpi.pattern.checkers.primitive.RegexChecker;
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
    private static final List<IssueChecker> checkers = List.of(
            new RegexChecker(
                    new Issue(
                            "test_issue",
                            "java main method signature"
                    ),
                    "public static void main"
            )
    );
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

        final List<RepoIssue> allIssues = repoFilesToBeAnalyzed
                .stream()
                .map(this::processFile)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return new RepoAnalysisResult(this.repoName, allIssues);
    }

    private List<RepoIssue> processFile(File fileToBeAnalyzed) {
        log.debug(() -> String.format("Start processing file %s", fileToBeAnalyzed));
        List<RepoIssue> foundedIssues = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileToBeAnalyzed))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                foundedIssues.addAll(
                        analyzeLine(line, lineNumber, fileToBeAnalyzed)
                                .collect(Collectors.toList()
                                )
                );

                ++lineNumber;
            }
        } catch (IOException e) {
            log.warn(() -> String.format(
                    "For error during processing file: %s. Error: %s",
                    fileToBeAnalyzed.getPath(),
                    e.getMessage()
            ));
            e.printStackTrace();
        }

        return foundedIssues;
    }

    private Stream<RepoIssue> analyzeLine(String line, int lineNumber, File file) {
        return RepoAnalyzer.checkers
                .stream()
                .map(checker -> checker.check(line))
                .filter(Objects::nonNull)
                .map(issue -> withFullInfo(
                        issue,
                        file.getName(),
                        file.getPath(),
                        lineNumber)
                );
    }

    private RepoIssue withFullInfo(Issue issue, String fileName, String filePath, int lineNumber) {
        return new RepoIssue(issue, fileName, filePath, lineNumber);
    }
}
