package com.kpi.master.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Data
public class Issue {
    private String issueType;
    private String issueDescription;
}
