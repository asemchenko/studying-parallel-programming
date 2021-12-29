package com.kpi.master.client;

import com.kpi.master.dto.RepoDto;
import com.kpi.master.dto.Report;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class Client {

    private RestTemplate restTemplate;

    @Async("asyncExecutor")
    public CompletableFuture<Report> sendTaskToWorker(RepoDto dto) throws InterruptedException
    {
        HttpEntity<RepoDto> request = new HttpEntity<>(dto);
        Report result = restTemplate.postForObject("http://localhost:8080/worker/analyze",request, Report.class);
        return CompletableFuture.completedFuture(result);
    }
}
