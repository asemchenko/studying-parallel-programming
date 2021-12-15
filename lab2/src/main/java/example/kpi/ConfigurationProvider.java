package example.kpi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.kpi.model.result.AppConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Log4j2
public class ConfigurationProvider {
    private final String[] cliArgs;

    private AppConfiguration configuration;


    /**
     * Must be called before getting configuration info
     */
    public AppConfiguration parseConfiguration() throws URISyntaxException, IOException {
        this.configuration = new AppConfiguration(
                List.of(
                        "https://github.com/asemchenko/Hotello-Spring.git",
//                        "https://github.com/maxliaops/Java_Web_Examples.git",
                        "https://github.com/eomjinyoung/JavaWebProgramming.git",
                        "https://github.com/Tastenkunst/brfv4_javascript_examples.git",
                        "https://github.com/cschneider4711/Marathon.git",
                        "https://github.com/mikemelon/java-signin.git",
                        "https://github.com/sonngotung/JWebMVC.git",
                        "https://github.com/tsultana2/EducationalWebSite.git",
                        "https://github.com/mikemelon/JavaWebEducation.git",
                        "https://github.com/Ocryst/Web3JavascriptEducation.git",
                        "https://github.com/mihail-petrov/netit-webdev-java.git",
                        "https://github.com/infinity23/family-education-platform.git"
                ),
                Integer.parseInt(cliArgs[2]),
                Integer.parseInt(cliArgs[3]),
                Path.of(cliArgs[0]),
                List.of(
                        "bin",
                        "zip"
                ),
                Paths.get(cliArgs[1]),
                Path.of(cliArgs[4])
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
