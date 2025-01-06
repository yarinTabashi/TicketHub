package com.yarin.movie.movie;

import com.yarin.movie.dtos.MovieMapper;
import com.yarin.movie.dtos.MovieResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository repository;
    private final MovieMapper mapper;

    public MovieService(MovieRepository movieRepository, MovieMapper mapper){
        this.repository = movieRepository;
        this.mapper = mapper;
    }

    public List<MovieResponse> findAll() {
        return  this.repository.findAll()
                .stream()
                .map(this.mapper::fromMovie)
                .collect(Collectors.toList());
    }

    public boolean isMovieExist(Integer movieId) {
        return repository.existsById(movieId);
    }
}
