package example.kpi.repo;

import example.kpi.di.Provider;
import example.kpi.model.result.AppConfiguration;
import example.kpi.model.result.RepoContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

@Log4j2
@RequiredArgsConstructor
public class RepoDownloader {
    private final AppConfiguration configuration = Provider.appConfiguration();
    private final String repoUrl;

    public RepoContent download() throws GitAPIException, IOException {
        log.debug(
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

        log.debug(
                () -> String.format(
                        "Cloned repo %s. Thread id: %d",
                        this.repoUrl,
                        Thread.currentThread().getId()
                )
        );

        return new RepoContent(directoryForRepoFiles);
    }

    private Path createRandomDirectoryForRepoFiles() throws IOException {
        final var directoryForRepoFiles = this.configuration
                .getRepoContentStoringDir()
                .resolve(Long.toString(Math.abs(new Random().nextLong())));
        Files.createDirectory(directoryForRepoFiles);
        log.debug(() -> String.format("Created directory for repo: %s. Thread id: %s", directoryForRepoFiles, Thread.currentThread().getId()));
        return directoryForRepoFiles;
    }
}
