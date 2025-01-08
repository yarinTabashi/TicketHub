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
        boolean[][] seatsAvailabilityMap = createSeatsAvailabilityMap(request.numOfRows(), request.seatsPerRow());

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

    // Helper method to create and initialize the seatsAvailabilityMap
    private boolean[][] createSeatsAvailabilityMap(int rowsCount, int seatsPerRow) {
        boolean[][] seats = new boolean[rowsCount][seatsPerRow];

        // Initialize all seats as available
        for (int row = 0; row < rowsCount; row++) {
            for (int col = 0; col < seatsPerRow; col++) {
                seats[row][col] = false; // False means available
            }
        }

        return seats;
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
