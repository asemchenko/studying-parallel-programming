package example.kpi.model.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RepoAnalysisResult {
    private final String repositoryName;
    private final List<RepoIssue> issues;
}
