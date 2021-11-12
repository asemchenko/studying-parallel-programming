package example.kpi;

import example.kpi.model.result.AppConfiguration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConfigurationProvider {
    private final String[] cliArgs;


    /**
     * Must be called before getting configuration info
     */
    public void parseConfiguration() {
        // TODO anse0220 implement me
    }

    public AppConfiguration getConfiguration() {
        // TODO anse0220 implement me;
        return null;
    }


}
