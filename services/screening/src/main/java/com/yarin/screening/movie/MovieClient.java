package com.yarin.screening.movie;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "movie-url",
        url = "${application.config.movie-url}"
)
public interface MovieClient {
    @GetMapping("/{movieId}/exists")
    boolean isMovieExist(@PathVariable("movieId") Integer movieId);
}