package com.batchExample.Movies.config;


import com.batchExample.Movies.entity.movie.Movie;
import org.springframework.batch.item.ItemProcessor;

public class MovieProcessor implements ItemProcessor<Movie, Movie> {

    @Override
    public Movie process(Movie movie) throws Exception {
        return movie;
    }
}
