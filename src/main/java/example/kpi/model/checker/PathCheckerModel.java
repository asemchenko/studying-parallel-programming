package example.kpi.model.checker;

import example.kpi.model.result.Issue;

public class PathCheckerModel {
    private Issue issue;
    private PathCheckerType type;
    private String pattern;
}

enum PathCheckerType {
    FILENAME,
    EXTENSION,
    PATH
}
