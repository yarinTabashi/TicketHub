package com.yarin.screening.DTOs;

import com.yarin.screening.Screening.Screening;
import org.springframework.stereotype.Component;

@Component
public class ScreeningMapper {
    public Screening toScreening(ScreeningRequest request){
        if (request == null){
            return null;
        }

        return Screening.builder()
                .movieId(request.movieId())
                .showtime(request.showtime())
                .availableSeats(request.availableSeats())
                .build();
    }

    public ScreeningResponse fromScreening(Screening screening) {
        if (screening == null) {
            return null;
        }
        return new ScreeningResponse(
                screening.getId(),
                screening.getMovieId(),
                screening.getShowtime(),
                screening.getAvailableSeats()
        );
    }
}
