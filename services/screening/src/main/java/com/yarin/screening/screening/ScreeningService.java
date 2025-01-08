package com.yarin.screening.screening;

import com.yarin.screening.dtos.ScreeningMapper;
import com.yarin.screening.dtos.ScreeningRequest;
import com.yarin.screening.dtos.ScreeningResponse;
import com.yarin.screening.dtos.SeatPosition;
import com.yarin.screening.exceptions.IncompatibilityException;
import com.yarin.screening.movie.MovieClient;
import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    /**
     * Validates whether a specific seat in a screening is available, and if so, reserves it.
     * The function uses on Optimistic Locking to ensure safe concurrent updates.
     *
     * @param screeningId The ID of the screening (must exist in the database).
     * @param seatNumber The seat number to validate and reserve (e.g., "A10", "B5", etc.).
     *
     * @return A `ResponseEntity<BigDecimal>` containing the ticket price if the seat is successfully reserved,
     *         or an appropriate error response:
     *         - {@code 404 Not Found} if the screening does not exist.
     *         - {@code 400 Bad Request} if the seat number is invalid (out of bounds).
     *         - {@code 409 Conflict} if the seat is already reserved (409 indicates a conflict - resource is unavailable).
     *         - {@code 200 OK} with the price of the ticket if the seat is successfully reserved.
     */
    public ResponseEntity<BigDecimal> validateAndReserveSeat(Integer screeningId, String seatNumber) {
        // Step 1: Fetch the screening from the repository
        Optional<Screening> optionalScreening = screeningRepository.findById(screeningId);

        if (optionalScreening.isEmpty()) {
            return ResponseEntity.notFound().build(); // Screening not found
        }

        Screening screening = optionalScreening.get();

        // Step 2: Pre-check: If no seats are available, skip further reservation logic.
        if (screening.getAvailableSeats() == 0){
            return ResponseEntity.status(409).body(null); // Seat not available
        }

        // Step 3: Parse the seatNumber
        SeatPosition seatPosition = parseSeatNumber(seatNumber);

        // Step 4: Access the seats availability map (boolean matrix)
        boolean[][] seatsAvailability = screening.getSeatsAvailabilityMap();

        // Step 5: Check if the seat is valid and available
        if (!isSeatAvailable(seatsAvailability, seatPosition)) {
            return ResponseEntity.status(409).body(null); // Seat already reserved or invalid seat number
        }

        // Step 6: Reserve the seat
        seatsAvailability[seatPosition.row()][seatPosition.col()] = true;
        screening.setSeatsAvailabilityMap(seatsAvailability);

        try {
            // Step 7: Save the updated screening with the reserved seat
            screeningRepository.save(screening);
        } catch (OptimisticLockException e) {
            // If a conflict occurs due to optimistic locking (if another user modified the screening)
            return ResponseEntity.status(409).body(null);
        }

        BigDecimal ticketPrice = screening.getTicketPrice();
        return ResponseEntity.ok(ticketPrice); // Successfully reserved
    }

    /**
     * Parse the seatNumber to determine the row (i) and column (j)
     * For example, "A10" is a seat number where A is the row, and 10 is the column
     * @return SeatNumber object: (i,j)
     */
    private SeatPosition parseSeatNumber(String seatNumber) {
        int row = seatNumber.charAt(0) - 'A'; // (The rows start from 'A')
        int col = Integer.parseInt(seatNumber.substring(1)) - 1; // Convert to 0-based index
        return new SeatPosition(row, col);
    }

    private boolean isSeatAvailable(boolean[][] seatsAvailability, SeatPosition seatPosition) {
        if (seatPosition.row() >= seatsAvailability.length || seatPosition.col() >= seatsAvailability[seatPosition.row()].length) {
            return false; // Invalid seat number
        }
        return !seatsAvailability[seatPosition.row()][seatPosition.col()]; // Seat is available if false
    }

    /**
     * Cancels the reservation for a specific seat in a screening.
     * The method handles optimistic locking to avoid conflicts in concurrent modifications.
     *
     * @param screeningId the ID of the screening
     * @param seatNumber the seat number (for example., "A10").
     * @return a {@link ResponseEntity} indicating the outcome of the cancellation attempt:
     *         - {@code 404 Not Found} if the screening does not exist.
     *         - {@code 400 Bad Request} if the seat was not reserved and thus cannot be canceled.
     *         - {@code 409 Conflict} if a concurrent modification occurs (optimistic locking failure).
     *         - {@code 204 No Content} if the reservation was successfully canceled.
     */
    public ResponseEntity<Void> cancelSeatReservation(Integer screeningId, String seatNumber) {
        // Step 1: Fetch the screening
        Optional<Screening> optionalScreening = screeningRepository.findById(screeningId);
        if (optionalScreening.isEmpty()) {
            return ResponseEntity.notFound().build(); // Screening not found
        }

        Screening screening = optionalScreening.get();

        // Step 2: Parse the seatNumber to determine the row (i) and column (j)
        SeatPosition seatPosition = parseSeatNumber(seatNumber);

        // Step 3: Access the seats availability map (boolean matrix)
        boolean[][] seatsAvailability = screening.getSeatsAvailabilityMap();

        // Step 4: Check if the seat is reserved
        if (isSeatAvailable(seatsAvailability, seatPosition)) {
            return ResponseEntity.status(400).body(null); // Seat was not reserved, so it's invalid request
        }

        // Step 5 : if really reserved, mark it as available
        seatsAvailability[seatPosition.row()][seatPosition.col()] = false;

        try {
            screening.setSeatsAvailabilityMap(seatsAvailability);
            screeningRepository.save(screening); // Optimistic locking will handle versioning
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(409).build(); // Conflict due to concurrent modification
        }

        return ResponseEntity.noContent().build(); // Successfully canceled the reservation
    }
}