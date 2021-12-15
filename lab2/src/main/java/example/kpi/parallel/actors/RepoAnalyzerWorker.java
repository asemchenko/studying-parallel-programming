package example.kpi.parallel.actors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.*;
import akka.pattern.StatusReply;
import example.kpi.model.result.RepoAnalysisResult;
import example.kpi.parallel.RepoAnalysisCallable;
import scala.concurrent.ExecutionContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class RepoAnalyzerWorker extends AbstractBehavior<RepoAnalyzerWorker.JobPayload> {
    final ExecutionContext ec = getContext()
            .getSystem()
            .dispatchers()
            .lookup(DispatcherSelector.fromConfig("my-blocking-dispatcher"));

    public static final class JobPayload {
        public final String repoUrl;
        public final ActorRef<StatusReply<RepoAnalysisResult>> replyTo;

        public JobPayload(String repoUrl, ActorRef<StatusReply<RepoAnalysisResult>> replyTo) {
            this.repoUrl = repoUrl;
            this.replyTo = replyTo;
        }
    }
    public static Behavior<JobPayload> create() {
        return Behaviors.setup(RepoAnalyzerWorker::new);
    }

    private RepoAnalyzerWorker(ActorContext<JobPayload> context) {
        super(context);
    }

    @Override
    public Receive<JobPayload> createReceive() {
        return newReceiveBuilder()
                .onMessage(JobPayload.class, this::onReceiveJobPayload)
                .build();
    }

    private Behavior<JobPayload> onReceiveJobPayload(JobPayload payload) {
        final var log = getContext().getLog();
        final var repoUrl = payload.repoUrl;
        final var replyTo = payload.replyTo;

        log.info("Worker received job payload to analyze repo: {}", payload.repoUrl);

        CompletableFuture.supplyAsync(() -> {
            try {
                final var result = RepoAnalysisCallable.create(repoUrl).call();
                replyTo.tell(StatusReply.success(result));
                log.info("done doing job for repo: {}", repoUrl);
            } catch (Exception e) {
                replyTo.tell(StatusReply.error(e.getMessage()));
            }
            return null;
        }, (Executor) ec);

        return this;
    }
}
