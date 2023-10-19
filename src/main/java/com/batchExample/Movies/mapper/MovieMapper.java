package com.batchExample.Movies.mapper;

import com.batchExample.Movies.entity.dto.MovieDto;
import com.batchExample.Movies.entity.movie.Movie;
import com.batchExample.Movies.repository.GenreRepository;
import com.batchExample.Movies.utils.EntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MovieMapper {
    public static Movie dtoToEntity(MovieDto movieDto, GenreRepository genreRepository, CacheManager cacheManager) {
        return Movie.builder()
                .title(movieDto.getTitle())
                .originalLanguage(EntityUtils.convertToLanguage(movieDto.getOriginalLanguage()))
                .releaseDate(EntityUtils.convertToMySQLDate(movieDto.getReleaseDate()))
                .popularity(movieDto.getPopularity())
                .budget(movieDto.getBudget())
                .revenue(movieDto.getRevenue())
                .runtime(movieDto.getRuntime())
                .status(EntityUtils.convertToStatus(movieDto.getStatus()))
                .genres(GenreMapper.parseGenres(movieDto.getGenres(), genreRepository, cacheManager))
                .build();
    }
}
