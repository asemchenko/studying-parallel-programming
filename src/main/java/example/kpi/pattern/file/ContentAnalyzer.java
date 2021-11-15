package example.kpi.pattern.file;

import example.kpi.model.result.Issue;
import example.kpi.model.result.RepoIssue;
import example.kpi.pattern.checkers.line.LineChecker;
import example.kpi.pattern.checkers.line.generic.RegexLineChecker;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class ContentAnalyzer {
    private static final List<LineChecker> checkers = List.of(
            new RegexLineChecker(
                    new Issue(
                            "test_issue",
                            "java main method signature"
                    ),
                    ".*public static void main.*"
            )
    );

    public List<RepoIssue> processFile(File fileToBeAnalyzed) {
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
        return ContentAnalyzer.checkers
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
