package com.kpi.worker.model.checker;

import com.kpi.worker.model.result.Issue;
import lombok.Data;

import java.util.UUID;

@Data
public class PathCheckerModel {
    private UUID id;
    private Issue issue;
    private PathCheckerType type;
    private String pattern;
}
