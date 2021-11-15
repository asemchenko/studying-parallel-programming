package example.kpi.model.result;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Issue {
    private String issueType;
    private String issueDescription;
}
