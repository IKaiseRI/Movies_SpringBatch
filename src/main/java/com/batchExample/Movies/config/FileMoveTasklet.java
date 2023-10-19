package com.batchExample.Movies.config;

import lombok.SneakyThrows;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.batchExample.Movies.constant.Constant.PROCESSED_FILE_PATH;
import static com.batchExample.Movies.constant.Constant.SOURCE_FILE_PATH;

public class FileMoveTasklet implements Tasklet {

    @Override
    @SneakyThrows
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        Path source = Path.of(SOURCE_FILE_PATH + "horrorMovies.csv");
        Path destination = Path.of(PROCESSED_FILE_PATH + "horrorMovies.csv");

        if(Files.exists(source)) {
            Files.move(source, destination);
        }

        return RepeatStatus.FINISHED;
    }
}
