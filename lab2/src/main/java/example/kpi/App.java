package example.kpi;

import example.kpi.di.Provider;
import example.kpi.parallel.ActorExecutor;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class App {
    public static void main(String[] args) {
        try {
            Provider.setAppConfiguration(new ConfigurationProvider(args).parseConfiguration());

            new ActorExecutor().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
