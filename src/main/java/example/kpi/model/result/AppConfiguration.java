package example.kpi.model.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class AppConfiguration {
    private final List<String> repoURLs;
    private final int threadsAmount;
    private final long timeoutSeconds;
}
