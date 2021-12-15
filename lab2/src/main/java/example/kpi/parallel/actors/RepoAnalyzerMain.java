package example.kpi.parallel.actors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import example.kpi.model.result.AppConfiguration;
import example.kpi.model.result.RepoAnalysisResult;
import example.kpi.model.result.Report;
import example.kpi.persistence.Persist;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class RepoAnalyzerMain extends AbstractBehavior<RepoAnalyzerMain.Command> {

    public interface Command {}

    private final List<RepoAnalysisResult> results = new ArrayList<>();
    private int expectedNumOfResults;

    public static class Start implements Command {
        public final AppConfiguration config;

        public Start(AppConfiguration config) {
            this.config = config;
        }
    }

    public static class WrappedWorkerResponse implements Command {
        final @Nullable RepoAnalysisResult result;
        final int id;

        WrappedWorkerResponse(@Nullable RepoAnalysisResult result, int id) {
            this.result = result;
            this.id = id;
        }
    }

    private static class CompletedAnalysis implements Command {
        CompletedAnalysis() {}
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(RepoAnalyzerMain::new);
    }

    private RepoAnalyzerMain(ActorContext<Command> context) {
        super(context);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Start.class, this::onStart)
                .onMessage(WrappedWorkerResponse.class, this::onWrappedWorkerResponse)
                .onMessage(CompletedAnalysis.class, this::onCompletedAnalysis)
                .build();
    }

    private Behavior<Command> onWrappedWorkerResponse(WrappedWorkerResponse wrappedResponse) {
        final var log = getContext().getLog();

        results.add(wrappedResponse.result);

        log.info("Got response from worker: {}", wrappedResponse.id);
        log.info("Got {} results so far", results.size());

        if (results.size() == expectedNumOfResults) {
            log.info("Got the last one!");
            getContext().getSelf().tell(new CompletedAnalysis());
        }

        return this;
    }

    private Behavior<Command> onCompletedAnalysis(CompletedAnalysis completedAnalysis) {
        final var log = getContext().getLog();

        final var validResults = results
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        final var report = new Report(validResults);

        try {
            Persist.saveReportToFile(report);
        } catch (IOException e) {
            log.error("Got error while saving file");
        }

        results.clear();

        return this;
    }

    private Behavior<Command> onStart(Start command) {
        final var config = command.config;
        final var repoUrls = config.getRepoURLs();
        expectedNumOfResults = repoUrls.size();

        final var jobTimeout = Duration.ofSeconds(config.getTimeoutSeconds());

        IntStream.range(0, repoUrls.size()).forEach(i -> {
            final var repoUrl = repoUrls.get(i);
            //#create-actors
            getContext().askWithStatus(
                    RepoAnalysisResult.class,
                    getContext().spawn(RepoAnalyzerWorker.create(), "Worker" + i),
                    jobTimeout,
                    (ref) -> new RepoAnalyzerWorker.JobPayload(repoUrl, ref),
                    (response, throwable) -> new WrappedWorkerResponse(response, i));
            //#create-actors
        });

        return this;
    }
}
