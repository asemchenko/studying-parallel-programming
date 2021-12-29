package com.kpi.worker.parallel;

import com.kpi.worker.model.result.AppConfiguration;
import com.kpi.worker.model.result.RepoAnalysisResult;
import com.kpi.worker.model.result.RepoContent;
import com.kpi.worker.pattern.RepoAnalyzer;
import com.kpi.worker.repo.RepoDownloader;
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
