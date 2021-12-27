package example.kpi.model.result;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RepoIssue {
    private Issue issue;
    private String path;
    private Integer lineNumber;
}
