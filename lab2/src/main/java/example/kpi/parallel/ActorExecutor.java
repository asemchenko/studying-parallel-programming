package example.kpi.parallel;

import akka.actor.typed.ActorSystem;
import example.kpi.di.Provider;
import example.kpi.model.result.AppConfiguration;
import example.kpi.parallel.actors.RepoAnalyzerMain;

public class ActorExecutor {
    final AppConfiguration config = Provider.appConfiguration();

    public void execute() {
        var masterActor = RepoAnalyzerMain.create();
        var actorSystem = ActorSystem.create(masterActor, "actorModelExecutor");

        actorSystem.tell(new RepoAnalyzerMain.Start(config));
    }
}
