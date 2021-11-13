package example.kpi;

import example.kpi.parallel.Executor;

public class App {
    public static void main(String[] args) {
        final var configurationProvider = new ConfigurationProvider(args);
        configurationProvider.parseConfiguration();

        final var executor = new Executor(
                configurationProvider.getConfiguration()
        );
        try {
            executor.execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
