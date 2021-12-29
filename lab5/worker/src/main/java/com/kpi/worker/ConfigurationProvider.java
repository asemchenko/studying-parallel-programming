package com.kpi.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpi.worker.dto.ReposDto;
import com.kpi.worker.model.result.AppConfiguration;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class ConfigurationProvider {

    private AppConfiguration configuration;


    /**
     * Must be called before getting configuration info
     */
    public AppConfiguration parseConfiguration(List<String> repos) throws URISyntaxException, IOException {
        this.configuration = new AppConfiguration(
                repos,
                4,
                1000,
                Path.of("/tmp"),
                List.of(
                        "bin",
                        "zip"
                ),
                Paths.get("/app/pathCheckers.json"),
                Path.of("/tmp")
        );
        log.info("Got app configuration: ");
        log.info(() -> {
            try {
                return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this.configuration);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return "";
            }
        });
        return this.configuration;
    }

    public AppConfiguration getConfiguration() {
        return this.configuration;
    }
}
