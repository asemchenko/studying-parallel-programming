package com.kpi.worker.controller;

import com.kpi.worker.dto.ReposDto;
import com.kpi.worker.model.result.Report;
import com.kpi.worker.service.AnalyzingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/worker")
public class Controller {

    private final AnalyzingService service;

    @PostMapping("/analyze")
    public Report analyze(@RequestBody ReposDto dto){
        return service.analyze(dto);
    }
}
