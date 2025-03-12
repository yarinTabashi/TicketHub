package com.yarin.screening;

import com.yarin.screening.dtos.ScreeningMapper;
import com.yarin.screening.dtos.ScreeningRequest;
import com.yarin.screening.dtos.ScreeningResponse;
import com.yarin.screening.movie.MovieClient;
import com.yarin.screening.screening.Screening;
import com.yarin.screening.screening.ScreeningRepository;
import com.yarin.screening.screening.ScreeningService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScreeningServiceTest {
    @Mock
    private ScreeningRepository repository;
    @Mock
    private ScreeningMapper mapper;
    @Mock
    private MovieClient movieClient;
    @InjectMocks
    private ScreeningService service;
    private static final int SCREENING_ID = 1, MOVIE_ID = 123;
    @Test
    public void createScreening_validInput_success(){
        ScreeningRequest request = Mockito.mock(ScreeningRequest.class);
        Mockito.when(movieClient.isMovieExist(Mockito.anyInt())).thenReturn(true);
        Screening screening = new Screening(SCREENING_ID, null, MOVIE_ID, null, null, null, null);
        Mockito.when(mapper.toScreening(request)).thenReturn(screening);
        Mockito.when(repository.save(screening)).thenReturn(screening);
        ScreeningResponse mockedResponse = new ScreeningResponse(SCREENING_ID, MOVIE_ID, null, null, null);
        Mockito.when(mapper.fromScreening(screening)).thenReturn(mockedResponse);

        ScreeningResponse response = service.createScreening(request);
        Assertions.assertEquals(SCREENING_ID, response.id());
        Assertions.assertEquals(MOVIE_ID, response.movieId());
    }
}
