package com.yarin.movie.dtos;

public record MovieRequest(
        String name,
        String description,
        Integer genreId
) {
}
