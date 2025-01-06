package com.yarin.movie.dtos;

import com.yarin.movie.movie.Movie;
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
