package example.kpi.model.result;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RepoIssue {
    private final Issue issue;
    private final String fileName;
    private final String filePath;
    private final int lineNumber;
}
