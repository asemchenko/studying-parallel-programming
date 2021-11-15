package example.kpi.pattern.file;

import example.kpi.model.result.RepoIssue;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class FileAnalyzer {
    private final ContentAnalyzer contentAnalyzer = new ContentAnalyzer();
    private final PathAnalyzer pathAnalyzer = new PathAnalyzer();


    public List<RepoIssue> processFile(File fileToBeAnalyzed) {
        final List<RepoIssue> contentIssues = contentAnalyzer.processFile(fileToBeAnalyzed);
        final List<RepoIssue> pathIssues = pathAnalyzer.processFile(fileToBeAnalyzed);

        return Stream.concat(
                contentIssues.stream(),
                pathIssues.stream()
        ).collect(Collectors.toList());
    }
}
