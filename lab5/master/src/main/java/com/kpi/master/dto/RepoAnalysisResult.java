package com.kpi.master.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepoAnalysisResult {
    private String repositoryName;
    private List<RepoIssue> issues;
}
