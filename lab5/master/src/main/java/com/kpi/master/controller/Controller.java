package com.kpi.master.controller;

import com.kpi.master.dto.RepoDto;
import com.kpi.master.dto.Report;
import com.kpi.master.dto.ResultDto;
import com.kpi.master.service.RepoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
@RequestMapping("/master")
public class Controller {

    private final RepoService service;

    @PostMapping
    public ResponseEntity<Report> analyze(@RequestBody RepoDto dto) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(service.sendTask(dto));
    }
}
