package com.kpi.worker.model.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.nio.file.Path;
import java.util.List;

@ToString
@RequiredArgsConstructor
@Getter
public class AppConfiguration {
    private final List<String> repoURLs;
    private final int threadsAmount;
    private final long timeoutSeconds;
    private final Path repoContentStoringDir;
    /**
     * Which files in repository should be analyzed. E.g. '.java', or '.ts'
     * Just to avoid analyzing some unnecessary files, like compiled binaries
     */
    private final List<String> fileExtensionsToBeSkipped;
    /**
     * Path to JSON file that contains path file checkers
     */
    private final Path pathCheckerDescriptor;
    private final Path reportFileOutput;
}
