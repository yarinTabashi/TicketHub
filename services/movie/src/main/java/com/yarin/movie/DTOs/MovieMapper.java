package com.yarin.movie.DTOs;

import com.yarin.movie.Movie.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {
    public MovieResponse fromMovie(Movie movie) {
        if (movie == null) {
            return null;
        }
        return new MovieResponse(
                movie.getId(),
                movie.getName(),
                movie.getDescription()
        );
    }
}
