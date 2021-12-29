package com.kpi.worker.service;

import com.kpi.worker.ConfigurationProvider;
import com.kpi.worker.dto.ReposDto;
import com.kpi.worker.model.result.AppConfiguration;
import com.kpi.worker.model.result.Report;
import com.kpi.worker.parallel.Executor;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
@AllArgsConstructor
@Log4j2
public class AnalyzingService {

    public Report analyze(ReposDto dto) {
        try {
            AppConfiguration configuration = new ConfigurationProvider().parseConfiguration(dto.getRepos());

            final var executor = new Executor();
            return executor.execute(configuration);
        } catch (InterruptedException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
