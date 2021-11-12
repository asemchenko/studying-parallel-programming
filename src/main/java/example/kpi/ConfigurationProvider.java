package example.kpi;

import example.kpi.model.result.AppConfiguration;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public class ConfigurationProvider {
    private final String[] cliArgs;

    private AppConfiguration configuration;


    /**
     * Must be called before getting configuration info
     */
    public void parseConfiguration() {
        this.configuration = new AppConfiguration(
                List.of(
                        "https://github.com/asemchenko/Hotello-Spring.git",
                        "https://github.com/maxliaops/Java_Web_Examples.git",
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
                4,
                60 * 60 * 60,
                Path.of("C:\\Users\\asem\\Downloads\\temp")
        );
        // TODO anse0220 implement me
    }

    public AppConfiguration getConfiguration() {
        return this.configuration;
    }


}
