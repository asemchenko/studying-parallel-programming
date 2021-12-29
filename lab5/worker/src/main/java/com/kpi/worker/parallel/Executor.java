package com.kpi.worker.parallel;

import com.kpi.worker.model.result.AppConfiguration;
import com.kpi.worker.model.result.RepoAnalysisResult;
import com.kpi.worker.model.result.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class Executor {

    private ExecutorService executorService;

    public Report execute(AppConfiguration configuration) throws InterruptedException {
        final List<RepoAnalysisResult> repoAnalysisResults = processRepositories(configuration);
        return new Report(repoAnalysisResults);
    }

    private List<RepoAnalysisResult> processRepositories(AppConfiguration configuration) throws InterruptedException {
        prepare(configuration);

        final var repoTasks = executorService.invokeAll(
                createTasksForProcessingRepositories(configuration),
                configuration.getTimeoutSeconds(),
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

    private void prepare(AppConfiguration configuration) {
        log.debug("Preparing executor service...");

        if (configuration.getThreadsAmount() == 1) {
            executorService = Executors.newSingleThreadExecutor();
        } else {
            executorService = Executors.newFixedThreadPool(configuration.getThreadsAmount());
        }
    }

    private void close() {
        log.debug("Closing resources...");
        this.executorService.shutdown();
    }

    private List<Callable<RepoAnalysisResult>> createTasksForProcessingRepositories(AppConfiguration configuration) {
        return configuration
                .getRepoURLs()
                .stream()
                .map(url -> RepoAnalysisCallable.create(url, configuration))
                .collect(Collectors.toList());
    }
}
