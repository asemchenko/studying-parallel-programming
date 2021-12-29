package com.kpi.master.service;

import com.kpi.master.client.Client;
import com.kpi.master.dto.RepoAnalysisResult;
import com.kpi.master.dto.RepoDto;
import com.kpi.master.dto.Report;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RepoService {
    private final Client client;

    public Report sendTask(RepoDto dto) throws ExecutionException, InterruptedException {
        List<String> repos = dto.getRepos();
        int numberOfRepos = repos.size();
        List<CompletableFuture<Report>> results = new ArrayList<>();
        for (int i = 0; i < numberOfRepos; i += 4) {
            try {
                results.add(client.sendTaskToWorker(new RepoDto(repos.subList(i, Math.min(i + 3, numberOfRepos)))));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CompletableFuture.allOf(results.toArray(new CompletableFuture<?>[0]))
                .thenApply(v -> results.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
        List<RepoAnalysisResult> completeResult = new ArrayList<>();
        for (CompletableFuture<Report> result : results) {
            completeResult.addAll(result.get().getRepoResults());
        }
        return new Report(completeResult);
    }
}
