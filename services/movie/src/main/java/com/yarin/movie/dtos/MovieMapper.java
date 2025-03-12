package com.yarin.movie.dtos;

import com.yarin.movie.genre.Genre;
import com.yarin.movie.genre.GenreRepository;
import com.yarin.movie.movie.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieMapper {
    private final GenreRepository genreRepository;

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

    public Movie toMovie(MovieRequest request){
        if (request == null){
            return null;
        }

        // Fetch Genre from the database using the genreId
        Genre genre = genreRepository.findById(request.genreId())
                .orElseThrow(() -> new RuntimeException("Genre not found"));

        return Movie.builder()
                .name(request.name())
                .description(request.description())
                .genre(genre)
                .build();
    }
}
