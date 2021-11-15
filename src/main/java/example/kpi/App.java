package example.kpi;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.kpi.model.result.AppConfiguration;
import example.kpi.model.result.Report;
import example.kpi.parallel.Executor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class App {
    public static void main(String[] args) {
        final var configurationProvider = new ConfigurationProvider(args);
        configurationProvider.parseConfiguration();

        final var executor = new Executor(
                configurationProvider.getConfiguration()
        );
        try {
            Report report = executor.execute();

            saveReportToFile(report, configurationProvider.getConfiguration());
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveReportToFile(Report report, AppConfiguration configuration) throws IOException {
        final String reportJson = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(report);
        final var reportFile = configuration.getRepoContentStoringDir().resolve(Path.of("report.json")).toFile();
        FileUtils.writeStringToFile(reportFile, reportJson, Charset.defaultCharset());
    }
}
