package example.kpi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.kpi.di.Provider;
import example.kpi.model.result.AppConfiguration;
import example.kpi.model.result.Report;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

@Log4j2
public class Persist {
    public static void saveReportToFile(Report report) throws IOException {
        AppConfiguration configuration = Provider.appConfiguration();

        final String reportJson = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(report);
        final var reportFile = configuration.getReportFileOutput().resolve(Path.of("report.json")).toFile();

        log.info(() -> String.format("Saving report to file: %s", reportFile.getAbsolutePath()));

        FileUtils.writeStringToFile(reportFile, reportJson, Charset.defaultCharset());

        log.info("[DONE] Report saved to file");
    }
}
