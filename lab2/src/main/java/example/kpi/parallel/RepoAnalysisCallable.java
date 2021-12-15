package example.kpi.parallel;

import example.kpi.model.result.RepoAnalysisResult;
import example.kpi.model.result.RepoContent;
import example.kpi.pattern.RepoAnalyzer;
import example.kpi.repo.RepoDownloader;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.Callable;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RepoAnalysisCallable implements Callable<RepoAnalysisResult> {
    private final String repoUrl;

    public static RepoAnalysisCallable create(String url) {
        return new RepoAnalysisCallable(url);
    }

    @Override
    public RepoAnalysisResult call() throws Exception {
        log.info(() -> String.format("Start downloading repo %s", repoUrl));

        final var repoDownloader = new RepoDownloader(repoUrl);
        final RepoContent repoContent = repoDownloader.download();

        log.info(() -> String.format("[DONE] Downloading repo %s finished", repoUrl));
        log.info(() -> String.format("[DONE] Start analyzing repo %s", repoUrl));

        final var repoAnalyzer = new RepoAnalyzer(this.repoUrl, repoContent);
        RepoAnalysisResult result = repoAnalyzer.analyze();

        log.info(() -> String.format("[DONE] Analyzing repo %s finished", repoUrl));

        return result;
    }
}
