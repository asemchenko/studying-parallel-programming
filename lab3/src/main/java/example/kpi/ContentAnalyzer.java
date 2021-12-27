package example.kpi;

import example.kpi.model.result.Issue;
import example.kpi.model.result.RepoIssue;
import example.kpi.line.LineChecker;
import example.kpi.line.specific.AwsAccessKeyChecker;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class ContentAnalyzer {
    private static final List<LineChecker> checkers = Arrays.asList(new AwsAccessKeyChecker());

    public List<RepoIssue> processFile(String path, String content) {
        List<RepoIssue> foundedIssues = new ArrayList<>();
        if (FilenameUtils.isExtension(path, "class", "jar", "bmp", "bin", "zip")) {
            return foundedIssues;
        }

        try (BufferedReader br = new BufferedReader(new StringReader(content))) {
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                foundedIssues.addAll(
                        analyzeLine(line, lineNumber, path).collect(Collectors.toList())
                );

                ++lineNumber;
            }
        } catch (IOException e) {
            log.warn(() -> String.format(
                    "For error during processing file: %s. Error: %s",
                    path,
                    e.getMessage()
            ));
            e.printStackTrace();
        }

        return foundedIssues;
    }


    private Stream<RepoIssue> analyzeLine(String line, int lineNumber, String path) {
        return ContentAnalyzer.checkers
                .stream()
                .map(checker -> checker.check(line))
                .filter(Objects::nonNull)
                .map(issue -> withFullInfo(
                        issue,
                        path,
                        lineNumber)
                );
    }


    private RepoIssue withFullInfo(Issue issue, String path, int lineNumber) {
        return new RepoIssue(issue, path, lineNumber);
    }
}
