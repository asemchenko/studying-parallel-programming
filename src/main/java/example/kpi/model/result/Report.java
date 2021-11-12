package example.kpi.model.result;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Report {
    private final List<RepoAnalysisResult> repoResults;
}
