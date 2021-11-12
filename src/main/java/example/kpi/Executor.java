package example.kpi;

import example.kpi.model.result.AppConfiguration;
import example.kpi.model.result.RepoContent;
import example.kpi.model.result.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class Executor {
    private final AppConfiguration configuration;

    private ExecutorService executorService;

    public Report execute() {
        final List<RepoContent> repoContents = downloadRepoContents();
        final List<>
        // TODO anse0220 implement me
        return null;
    }

    private List<RepoContent> downloadRepoContents() {
        prepare();

        List<Future<RepoContent>> downloadTasks = null;
        try {
            downloadTasks = executorService.invokeAll(
                    createTasksForDownloadingRepoContent(),
                    this.configuration.getTimeoutSeconds(),
                    TimeUnit.SECONDS
            );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // removing tasks that finished with errors
        return downloadTasks.stream().map(task -> {
            try {
                return task.get();
            } catch (InterruptedException | ExecutionException e) {
                // TODO anse0220 put logging here
                return null;
            }
        }).collect(Collectors.toList());
    }

    private void prepare() {
        if (this.configuration.getThreadsAmount() == 1) {
            executorService = Executors.newSingleThreadExecutor();
        } else {
            executorService = Executors.newFixedThreadPool(this.configuration.getThreadsAmount());
        }
    }

    private <T> Future<T> runTask(Callable<T> task) {
        return this.executorService.submit(task);
    }

    private List<Callable<RepoContent>> createTasksForDownloadingRepoContent() {
        // TODO anse0220 implement me
        return null;
    }
}
