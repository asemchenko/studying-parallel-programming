package example.kpi.model.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RepoIssue {
    private final Issue issue;
    private final String fileName;
    private final String filePath;
    private final Integer lineNumber;
}
