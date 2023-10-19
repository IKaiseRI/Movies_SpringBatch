package com.batchExample.Movies.config;

import com.batchExample.Movies.entity.dto.MovieDto;
import com.batchExample.Movies.entity.movie.Movie;
import com.batchExample.Movies.repository.GenreRepository;
import com.batchExample.Movies.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import static com.batchExample.Movies.constant.Constant.MAIN_CSV;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
@EnableCaching
public class CsvItemConfiguration {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private MovieRepository movieRepository;
    private GenreRepository genreRepository;

    @Bean
    public FlatFileItemReader<MovieDto> reader() {
        FlatFileItemReader<MovieDto> movieReader = new FlatFileItemReader<>();

        movieReader.setResource(new FileSystemResource(MAIN_CSV));
        movieReader.setName("csvReader");
        movieReader.setLinesToSkip(1);
        movieReader.setLineMapper(getMovieLineMapper());

        return movieReader;
    }

    public LineMapper<MovieDto> getMovieLineMapper() {
        DefaultLineMapper<MovieDto> movieLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        BeanWrapperFieldSetMapper<MovieDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();

        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("title", "originalLanguage", "releaseDate", "popularity", "budget", "revenue", "runtime", "status", "genres");

        fieldSetMapper.setTargetType(MovieDto.class);

        movieLineMapper.setLineTokenizer(delimitedLineTokenizer);
        movieLineMapper.setFieldSetMapper(fieldSetMapper);

        return movieLineMapper;
    }

    @Bean
    public MovieProcessor processor() {
        return new MovieProcessor(genreRepository, cacheManager());
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("genres");
    }

    @Bean
    public RepositoryItemWriter<Movie> writer() {
        RepositoryItemWriter<Movie> movieWriter = new RepositoryItemWriter<>();
        movieWriter.setRepository(movieRepository);
        movieWriter.setMethodName("save");

        return movieWriter;
    }

    @Bean
    public Step stepProcess() {
        return stepBuilderFactory.get("csvMovie-step").<MovieDto, Movie>chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Step stepMoveFile() {
        return stepBuilderFactory.get("moveFileStep")
                .tasklet(new FileMoveTasklet())
                .build();
    }

    @Bean
    public Job jobRun() {
        return jobBuilderFactory.get("importMovies")
                .flow(stepProcess())
                .next(stepMoveFile())
                .end()
                .build();
    }
}
