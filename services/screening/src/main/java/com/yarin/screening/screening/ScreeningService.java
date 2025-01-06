package com.yarin.screening.screening;

import com.yarin.screening.dtos.ScreeningMapper;
import com.yarin.screening.dtos.ScreeningRequest;
import com.yarin.screening.dtos.ScreeningResponse;
import com.yarin.screening.exceptions.IncompatibilityException;
import com.yarin.screening.movie.MovieClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScreeningService {
    private final ScreeningRepository screeningRepository;
    private final ScreeningMapper screeningMapper;
    private final MovieClient movieClient;

    // Create a new screening
    public ScreeningResponse createScreening(ScreeningRequest request) {
        boolean isMovieExists = movieClient.isMovieExist(request.movieId());
        if (!isMovieExists){
            throw new IncompatibilityException("Cannot create the screening, because no movie exist with this id.");
        }
        Screening savedScreening = screeningRepository.save(screeningMapper.toScreening(request));
        return screeningMapper.fromScreening(savedScreening);  // Map the saved screening to ScreeningResponse
    }

    // Get all screenings
    public List<ScreeningResponse> getAllScreenings() {
        return this.screeningRepository.findAll()
                .stream()
                .map(this.screeningMapper::fromScreening) // Map each Screening to a ScreeningResponse
                .collect(Collectors.toList());
    }

    // Get a screening by ID
    public Optional<ScreeningResponse> getScreeningById(Integer id) {
        return screeningRepository.findById(id)
                .map(this.screeningMapper::fromScreening);  // Map the found Screening to a ScreeningResponse
    }

    // Delete a screening by ID
    public void deleteScreening(Integer id) {
        screeningRepository.deleteById(id);
    }

    // Get screenings by movieId
    public List<ScreeningResponse> getScreeningsByMovieId(Integer movieId) {
        return screeningRepository.findByMovieId(movieId)
                .stream()
                .map(this.screeningMapper::fromScreening)  // Map each Screening to a ScreeningResponse
                .collect(Collectors.toList());
    }

    public int getRemainingTickets(Integer screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("Screening not found"));

        return screening.getAvailableSeats();
    }
}