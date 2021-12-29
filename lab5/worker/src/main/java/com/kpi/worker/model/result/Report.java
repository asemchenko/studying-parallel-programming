package com.kpi.worker.model.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Report {
    private final List<RepoAnalysisResult> repoResults;
}
