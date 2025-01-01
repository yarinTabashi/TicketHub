package com.yarin.screening.Screening;

import com.yarin.screening.DTOs.ScreeningRequest;
import com.yarin.screening.DTOs.ScreeningResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/screening")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    // Create a new screening
    @PostMapping
    public ResponseEntity<ScreeningResponse> createScreening(@RequestBody ScreeningRequest screening) {
        ScreeningResponse createdScreening = screeningService.createScreening(screening);
        return new ResponseEntity<>(createdScreening, HttpStatus.CREATED);
    }

    // Get all screenings
    @GetMapping
    public ResponseEntity<List<ScreeningResponse>> getAllScreenings() {
        List<ScreeningResponse> screenings = screeningService.getAllScreenings();
        return new ResponseEntity<>(screenings, HttpStatus.OK);
    }

    // Get a screening by ID
    @GetMapping("/{id}")
    public ResponseEntity<ScreeningResponse> getScreeningById(@PathVariable Integer id) {
        Optional<ScreeningResponse> screening = screeningService.getScreeningById(id);
        return screening.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a screening by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScreening(@PathVariable Integer id) {
        screeningService.deleteScreening(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint to get screenings by movieId
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ScreeningResponse>> getScreeningsByMovieId(@PathVariable Integer movieId) {
        List<ScreeningResponse> screenings = screeningService.getScreeningsByMovieId(movieId);
        return ResponseEntity.ok(screenings);
    }

    @GetMapping("/remaining-tickets/{screening-id}")
    public ResponseEntity<Integer> getRemainingTickets(@PathVariable("screening-id") Integer screeningId) {
        int remainingTickets = screeningService.getRemainingTickets(screeningId);
        return ResponseEntity.ok(remainingTickets);
    }
}