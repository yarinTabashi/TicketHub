package com.yarin.screening.screening;

import com.yarin.screening.dtos.ScreeningRequest;
import com.yarin.screening.dtos.ScreeningResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/screening")
@RequiredArgsConstructor
public class ScreeningController {
    private final ScreeningService screeningService;

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

    // Get all the screening by movieId
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ScreeningResponse>> getScreeningsByMovieId(@PathVariable Integer movieId) {
        List<ScreeningResponse> screenings = screeningService.getScreeningsByMovieId(movieId);
        return ResponseEntity.ok(screenings);
    }

//    @GetMapping("/validateAndReserveSeat/{screening-id}/{seat-number}")
//    public CompletableFuture<ResponseEntity<BigDecimal>> validateAndReserveSeat(
//            @PathVariable("screening-id") Integer screeningId,
//            @PathVariable("seat-number") String seatNumber) {
//
//        // Call the service asynchronously
//        return screeningService.validateAndReserveSeat(screeningId, seatNumber)
//                .thenApply(response -> {
//                    // Handle the response from the async service method
//                    if (response.getStatusCode().is2xxSuccessful()) {
//                        // If validation succeeds, return OK response with price
//                        return ResponseEntity.ok(response.getBody());
//                    } else {
//                        // If validation fails, returns the error code
//                        return ResponseEntity.status(response.getStatusCode()).body(null);
//                    }
//                });
//    }
//
//    @DeleteMapping("/cancel/{screening-id}/{seat-number}")
//    ResponseEntity<Void> cancelSeatReservation(@PathVariable("screening-id") Integer screeningId,
//                                               @PathVariable("seat-number") String seatNumber){
//        return screeningService.cancelSeatReservation(screeningId, seatNumber);
//    }
}