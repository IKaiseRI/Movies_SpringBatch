package com.batchExample.Movies.mapper;

import com.batchExample.Movies.entity.genre.Genre;
import com.batchExample.Movies.repository.GenreRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreMapper {

    public static Set<Genre> parseGenres(String genreString, GenreRepository genreRepository, CacheManager cacheManager) {
        List<String> genreNames = Arrays.stream(genreString.split(", ")).toList();

        Set<Genre> genres = new HashSet<>();

        for (String genreName : genreNames) {
            Cache cache = cacheManager.getCache("genres");
            Genre existingGenre = cache.get(genreName, Genre.class);

            if (existingGenre == null) {
                existingGenre = genreRepository.findByName(genreName).orElse(null);

                if (existingGenre == null) {
                    existingGenre = new Genre();
                    existingGenre.setName(genreName);
                    genreRepository.save(existingGenre);
                }

                cache.put(genreName, existingGenre);
            }

            genres.add(existingGenre);
        }

        return genres;
    }
}
