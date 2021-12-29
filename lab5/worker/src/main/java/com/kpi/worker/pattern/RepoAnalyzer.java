package com.kpi.worker.pattern;

import com.kpi.worker.di.Provider;
import com.kpi.worker.model.result.AppConfiguration;
import com.kpi.worker.model.result.RepoAnalysisResult;
import com.kpi.worker.model.result.RepoContent;
import com.kpi.worker.model.result.RepoIssue;
import com.kpi.worker.pattern.file.FileAnalyzer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
public class RepoAnalyzer {
    private final AppConfiguration configuration = Provider.appConfiguration();
    private final String repoName;
    private final RepoContent repoContent;

    public RepoAnalysisResult analyze() {
        Collection<File> repoFilesToBeAnalyzed = FileUtils.listFiles(
                        repoContent.getRepoDirectory().toFile(),
                        null,
                        true
                ).stream()
                .filter(f -> !this.configuration.getFileExtensionsToBeSkipped().contains(
                        FilenameUtils.getExtension(f.getName())
                ))
                .collect(Collectors.toList());

        log.debug(() -> String.format(
                "Start analyzing repo %s. Got files to be checked: %s",
                repoName,
                repoFilesToBeAnalyzed)
        );

        final FileAnalyzer fileAnalyzer = new FileAnalyzer();
p
        final Stream<RepoIssue> allIssues = repoFilesToBeAnalyzed
                .stream()
                .map(fileAnalyzer::processFile)
                .flatMap(List::stream)
                ;

        return new RepoAnalysisResult(this.repoName, allIssues.collect(Collectors.toList()));
    }
}
