package example.kpi.model.result;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RepoAnalysisResult {
    private final List<RepoIssue> issues;
}
