package example.kpi.parallel;

import example.kpi.model.result.AppConfiguration;
import example.kpi.model.result.RepoAnalysisResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.Git;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.concurrent.Callable;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RepoProcessingCallable implements Callable<RepoAnalysisResult> {
    private final String repoUrl;
    private final AppConfiguration configuration;

    public static RepoProcessingCallable create(String url, AppConfiguration configuration) {
        return new RepoProcessingCallable(url, configuration);
    }

    @Override
    public RepoAnalysisResult call() throws Exception {
        log.info(
                () -> String.format(
                        "Start cloning repo %s. Thread id: %d",
                        this.repoUrl,
                        Thread.currentThread().getId()
                )
        );

        final var directoryForRepoFiles = createRandomDirectoryForRepoFiles();

        Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(directoryForRepoFiles.toFile())
                .call();

        log.info(
                () -> String.format(
                        "Cloned repo %s. Thread id: %d",
                        this.repoUrl,
                        Thread.currentThread().getId()
                )
        );
        return null;
    }

    private Path createRandomDirectoryForRepoFiles() throws IOException {
        final var directoryForRepoFiles = this.configuration
                .getRepoContentStoringDir()
                .resolve(Long.toString(Math.abs(new Random().nextLong())));
        Files.createDirectory(directoryForRepoFiles);
        log.info(() -> String.format("Created directory for repo: %s. Thread id: %s", directoryForRepoFiles, Thread.currentThread().getId()));
        return directoryForRepoFiles;
    }
}
