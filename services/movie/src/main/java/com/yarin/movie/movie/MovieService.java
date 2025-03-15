package com.yarin.movie.movie;
import com.yarin.movie.dtos.MovieMapper;
import com.yarin.movie.dtos.MovieRequest;
import com.yarin.movie.dtos.MovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository repository;
    private final MovieMapper mapper;

    public List<MovieResponse> findAll() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromMovie)
                .collect(Collectors.toList());
    }

    public MovieResponse createMovie(MovieRequest movieRequest) {
        Movie movie = mapper.toMovie(movieRequest);
        Movie savedMovie = repository.save(movie);
        return mapper.fromMovie(savedMovie);
    }
}
