package com.kpi.master.dto;

import lombok.*;

import java.nio.file.Path;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepoContent {
    private Path repoDirectory;
}
