package com.yarin.movie;

import com.yarin.movie.dtos.MovieMapper;
import com.yarin.movie.dtos.MovieResponse;
import com.yarin.movie.movie.Movie;
import com.yarin.movie.movie.MovieRepository;
import com.yarin.movie.movie.MovieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    @Mock
    private MovieRepository repository;
    @Mock
    private MovieMapper mapper;
    @InjectMocks
    private MovieService service;

    @Test
    public void findAll_shouldReturnAll(){
        Movie movie1 = new Movie(1, null, null, null);
        Movie movie2 = new Movie(2, null, null, null);
        List<Movie> movies = Arrays.asList(movie1, movie2);
        Mockito.when(repository.findAll()).thenReturn(movies);

        MovieResponse movieResponse1 = new MovieResponse(1, null, null);
        MovieResponse movieResponse2 = new MovieResponse(2, null, null);

        Mockito.when(mapper.fromMovie(movie1)).thenReturn(movieResponse1);
        Mockito.when(mapper.fromMovie(movie2)).thenReturn(movieResponse2);

        List<MovieResponse> result = service.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Mockito.verify(repository, Mockito.times(1)).findAll();
        Mockito.verify(mapper, Mockito.times(2)).fromMovie(Mockito.any(Movie.class));
    }

    @Test
    public void findAll_shouldReturnEmptyList(){
        List<Movie> movies = new ArrayList<>();
        Mockito.when(repository.findAll()).thenReturn(movies);
        List<MovieResponse> result = service.findAll();

        Assertions.assertEquals(0, result.size());
        Mockito.verify(repository, Mockito.times(1)).findAll();
        Mockito.verify(mapper, Mockito.times(0)).fromMovie(Mockito.any(Movie.class));
    }
}
