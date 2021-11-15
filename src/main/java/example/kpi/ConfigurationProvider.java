package example.kpi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import example.kpi.model.result.AppConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public AppConfiguration parseConfiguration() throws URISyntaxException {
        this.configuration = new AppConfiguration(
                List.of(
                        "https://github.com/asemchenko/Hotello-Spring.git"
//                        "https://github.com/maxliaops/Java_Web_Examples.git",
//                        "https://github.com/eomjinyoung/JavaWebProgramming.git",
//                        "https://github.com/Tastenkunst/brfv4_javascript_examples.git",
//                        "https://github.com/cschneider4711/Marathon.git",
//                        "https://github.com/mikemelon/java-signin.git",
//                        "https://github.com/sonngotung/JWebMVC.git",
//                        "https://github.com/tsultana2/EducationalWebSite.git",
//                        "https://github.com/mikemelon/JavaWebEducation.git",
//                        "https://github.com/Ocryst/Web3JavascriptEducation.git",
//                        "https://github.com/mihail-petrov/netit-webdev-java.git",
//                        "https://github.com/infinity23/family-education-platform.git"
                ),
                4,
                60 * 60 * 60,
                Path.of("C:\\Users\\asem\\Downloads\\temp"),
                List.of(
                        "java",
                        "xml",
                        "json",
                        "yaml"
                ),
                Paths.get(Objects.requireNonNull(ClassLoader.getSystemResource("pathCheckers.json")).toURI())
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
