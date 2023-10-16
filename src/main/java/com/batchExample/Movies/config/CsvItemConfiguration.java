package com.batchExample.Movies.config;

import com.batchExample.Movies.entity.genre.Genre;
import com.batchExample.Movies.entity.movie.Movie;
import com.batchExample.Movies.repository.GenreRepository;
import com.batchExample.Movies.repository.MovieRepository;
import com.batchExample.Movies.utils.EntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.batchExample.Movies.constant.Constant.DEMO_CSV;
import static com.batchExample.Movies.constant.Constant.MAIN_CSV;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class CsvItemConfiguration {
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private MovieRepository movieRepository;
    private GenreRepository genreRepository;

    @Bean
    public FlatFileItemReader<Movie> reader() {
        FlatFileItemReader<Movie> movieReader = new FlatFileItemReader<>();

        movieReader.setResource(new FileSystemResource(DEMO_CSV));
        movieReader.setName("csvReader");
        movieReader.setLinesToSkip(1);
        movieReader.setLineMapper(getMovieLineMapper());

        return movieReader;
    }

    public LineMapper<Movie> getMovieLineMapper() {
        DefaultLineMapper<Movie> movieLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        FieldSetMapper<Movie> beanMapper = createBeanMapper();

        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);
        delimitedLineTokenizer.setNames("title", "originalLanguage", "releaseDate", "popularity", "budget", "revenue", "runtime", "status", "genres");

        movieLineMapper.setLineTokenizer(delimitedLineTokenizer);
        movieLineMapper.setFieldSetMapper(beanMapper);

        return movieLineMapper;
    }

    private FieldSetMapper<Movie> createBeanMapper() {
        return fieldSet -> {
            Movie movie = new Movie();
            movie.setTitle(fieldSet.readString("title"));
            movie.setOriginalLanguage(fieldSet.readString("originalLanguage"));
            movie.setReleaseDate(EntityUtils.convertToMySQLDate(fieldSet.readString("releaseDate")));
            movie.setPopularity(fieldSet.readDouble("popularity"));
            movie.setBudget(fieldSet.readLong("budget"));
            movie.setRevenue(fieldSet.readLong("revenue"));
            movie.setRuntime(fieldSet.readInt("runtime"));
            movie.setStatus(fieldSet.readString("status"));
            Set<Genre> genres = parseGenres(fieldSet.readString("genres"));
            movie.setGenres(genres);
            return movie;
        };
    }

    private Set<Genre> parseGenres(String genreString) {
        List<String> genreNames = Arrays.stream(genreString.split(", ")).toList();

        Set<Genre> genres = new HashSet<>();

        for (String genreName : genreNames) {
            Genre existingGenre = genreRepository.findByName(genreName).orElse(null);

            if (existingGenre == null) {
                existingGenre = new Genre();
                existingGenre.setName(genreName);
                genreRepository.save(existingGenre);
            }

            genres.add(existingGenre);
        }

        return genres;
    }

    @Bean
    public MovieProcessor processor() {
        return new MovieProcessor();
    }

    @Bean
    public RepositoryItemWriter<Movie> writer() {
        RepositoryItemWriter<Movie> movieWriter = new RepositoryItemWriter<>();
        movieWriter.setRepository(movieRepository);
        movieWriter.setMethodName("save");

        return movieWriter;
    }

    @Bean
    public Step stepOne() {
        return stepBuilderFactory.get("csvMovie-step").<Movie, Movie>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job jobRun() {
        return jobBuilderFactory.get("importMovies")
                .flow(stepOne())
                .end()
                .build();
    }
}
