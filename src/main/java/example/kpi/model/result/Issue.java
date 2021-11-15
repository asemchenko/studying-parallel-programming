package example.kpi.model.result;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class Issue {
    private final String issueType;
    private final String issueDescription;
}
