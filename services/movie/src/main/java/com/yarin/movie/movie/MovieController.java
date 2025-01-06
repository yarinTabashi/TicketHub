package com.yarin.movie.movie;

import com.yarin.movie.dtos.MovieResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/movies")
//@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieResponse>> findAll() {
        return ResponseEntity.ok(this.movieService.findAll());
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> isMovieExist(@RequestParam("movieId") Integer movieId) {
        boolean exists = this.movieService.isMovieExist(movieId);
        return ResponseEntity.ok(exists);
    }
}
