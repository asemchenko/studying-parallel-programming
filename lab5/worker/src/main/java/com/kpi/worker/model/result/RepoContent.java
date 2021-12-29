package com.kpi.worker.model.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;

@Getter
@RequiredArgsConstructor
public class RepoContent {
    private final Path repoDirectory;
}
