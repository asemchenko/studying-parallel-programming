package com.kpi.master.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private List<RepoAnalysisResult> repoResults = new ArrayList<>();
}
