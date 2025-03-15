package com.yarin.movie.movie;

import com.yarin.movie.dtos.MovieRequest;
import com.yarin.movie.dtos.MovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(@RequestBody MovieRequest movieRequest) {
        MovieResponse createdMovie = movieService.createMovie(movieRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> findAll() {
        return ResponseEntity.ok(this.movieService.findAll());
    }
}