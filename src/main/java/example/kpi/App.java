package example.kpi;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args )
    {
        final var configurationProvider = new ConfigurationProvider(args);
        configurationProvider.parseConfiguration();

        final var executor = new Executor(
                configurationProvider.getConfiguration()
        );
        executor.execute();
    }
}
