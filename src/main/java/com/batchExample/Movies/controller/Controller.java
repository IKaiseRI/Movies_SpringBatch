package com.batchExample.Movies.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class Controller {
    private JobLauncher jobLauncher;
    private Job job;

    @SneakyThrows
    @PostMapping("/importMovies")
    public void importFromCsv() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startOn", System.currentTimeMillis()).toJobParameters();

        jobLauncher.run(job, jobParameters);
    }
}
