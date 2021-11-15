package example.kpi.parallel;

import example.kpi.di.Provider;
import example.kpi.model.result.AppConfiguration;
import example.kpi.model.result.RepoAnalysisResult;
import example.kpi.model.result.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class Executor {
    private final AppConfiguration configuration = Provider.appConfiguration();

    private ExecutorService executorService;

    public Report execute() throws InterruptedException {
        final List<RepoAnalysisResult> repoAnalysisResults = processRepositories();
        return new Report(repoAnalysisResults);
    }

    private List<RepoAnalysisResult> processRepositories() throws InterruptedException {
        prepare();

        final var repoTasks = executorService.invokeAll(
                createTasksForProcessingRepositories(),
                this.configuration.getTimeoutSeconds(),
                TimeUnit.SECONDS
        );

        close();

        return filterSuccessTasks(repoTasks);
    }

    private List<RepoAnalysisResult> filterSuccessTasks(List<Future<RepoAnalysisResult>> repoTasks) {
        // removing tasks that finished with errors
        return repoTasks.stream().map(task -> {
            try {
                return task.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error(() -> String.format("Task finished with error: %s", e));
                return null;
            }
        }).collect(Collectors.toList());
    }

    private void prepare() {
        log.debug("Preparing executor service...");

        if (this.configuration.getThreadsAmount() == 1) {
            executorService = Executors.newSingleThreadExecutor();
        } else {
            executorService = Executors.newFixedThreadPool(this.configuration.getThreadsAmount());
        }
    }

    private void close() {
        log.debug("Closing resources...");
        this.executorService.shutdown();
    }

    private List<Callable<RepoAnalysisResult>> createTasksForProcessingRepositories() {
        return this.configuration
                .getRepoURLs()
                .stream()
                .map(url -> RepoAnalysisCallable.create(url, this.configuration))
                .collect(Collectors.toList());
    }
}
