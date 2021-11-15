package example.kpi.model.checker;

import example.kpi.model.result.Issue;
import lombok.Data;

@Data
public class PathCheckerModel {
    private Issue issue;
    private PathCheckerType type;
    private String pattern;
}
