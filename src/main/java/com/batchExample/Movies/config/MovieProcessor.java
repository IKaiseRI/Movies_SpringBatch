package com.batchExample.Movies.config;


import com.batchExample.Movies.entity.dto.MovieDto;
import com.batchExample.Movies.entity.movie.Movie;
import com.batchExample.Movies.exception.ErrorMessage;
import com.batchExample.Movies.mapper.MovieMapper;
import com.batchExample.Movies.repository.GenreRepository;
import com.batchExample.Movies.validation.CustomValidation;
import com.opencsv.CSVWriter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.io.FileWriter;

import static com.batchExample.Movies.constant.Constant.FAILED_CSV;

@Component
@AllArgsConstructor
public class MovieProcessor implements ItemProcessor<MovieDto, Movie> {
    private GenreRepository genreRepository;
    private CacheManager cacheManager;

    @Override
    public Movie process(MovieDto movieDto) {
        String message = CustomValidation.isValidMovie(
                movieDto.getOriginalLanguage(),
                movieDto.getReleaseDate(),
                movieDto.getStatus()
        );

        if (message == null) {
            return MovieMapper.dtoToEntity(movieDto, genreRepository, cacheManager);
        }

        writeFailedMovies(movieDto, message);
        return null;
    }

    @SneakyThrows
    public void writeFailedMovies(MovieDto movieDto, String message) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(FAILED_CSV, true))) {
            String[] record = {
                    movieDto.getTitle(),
                    movieDto.getOriginalLanguage(),
                    movieDto.getReleaseDate(),
                    String.valueOf(movieDto.getPopularity()),
                    String.valueOf(movieDto.getBudget()),
                    String.valueOf(movieDto.getRevenue()),
                    String.valueOf(movieDto.getRuntime()),
                    movieDto.getStatus(),
                    movieDto.getGenres(),
                    ErrorMessage.failedCustomValidationMessage(message)
            };

            writer.writeNext(record);
        }
    }

}
