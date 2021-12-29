package com.kpi.master.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepoIssue {
    private Issue issue;
    private String fileName;
    private String filePath;
    private Integer lineNumber;
}
