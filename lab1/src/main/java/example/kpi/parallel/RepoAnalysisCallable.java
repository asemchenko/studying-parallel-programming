package example.kpi.parallel;

import example.kpi.model.result.AppConfiguration;
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

    public static RepoAnalysisCallable create(String url, AppConfiguration configuration) {
        return new RepoAnalysisCallable(url);
    }

    @Override
    public RepoAnalysisResult call() throws Exception {
        final var repoDownloader = new RepoDownloader(repoUrl);
        final RepoContent repoContent = repoDownloader.download();

        final var repoAnalyzer = new RepoAnalyzer(this.repoUrl, repoContent);
        return repoAnalyzer.analyze();
    }
}
