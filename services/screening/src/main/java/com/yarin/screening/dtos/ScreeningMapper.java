package com.yarin.screening.dtos;

import com.yarin.screening.screening.Screening;
import org.springframework.stereotype.Component;

@Component
public class ScreeningMapper {
    public Screening toScreening(ScreeningRequest request){
        if (request == null){
            return null;
        }

        // Initialize the seatsAvailabilityMap as a 2D array
        String seatsAvailabilityMap = createSeatsAvailabilityMap(request.numOfRows(), request.seatsPerRow());

        // Calculate the total number of available seats
        Integer availableSeats = request.numOfRows() * request.seatsPerRow();

        return Screening.builder()
                .movieId(request.movieId())
                .showtime(request.showtime())
                .seatsAvailabilityMap(seatsAvailabilityMap)
                .availableSeats(availableSeats)
                .ticketPrice(request.ticketPrice())
                .build();
    }

    // Initialize the seatsAvailabilityMap (at the beginning, all the cells are available)
    private String createSeatsAvailabilityMap(int rowsCount, int seatsPerRow) {
        StringBuilder availabilityMap = new StringBuilder();

        // Loop through each row and initialize all seats as available (represented by 'T')
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < seatsPerRow; j++) {
                availabilityMap.append("0");
            }
            if (i < rowsCount - 1) {
                availabilityMap.append("\n");
            }
        }

        return availabilityMap.toString();
    }

    public ScreeningResponse fromScreening(Screening screening) {
        if (screening == null) {
            return null;
        }
        return new ScreeningResponse(
                screening.getId(),
                screening.getMovieId(),
                screening.getShowtime(),
                screening.getAvailableSeats(),
                screening.getTicketPrice()
        );
    }
}
