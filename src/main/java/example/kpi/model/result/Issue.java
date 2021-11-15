package example.kpi.model.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Issue {
    private final String issueType;
    private final String issueDescription;
}
