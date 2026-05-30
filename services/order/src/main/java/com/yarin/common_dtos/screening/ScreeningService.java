package com.yarin.common_dtos.screening;

import com.yarin.common_dtos.common_dtos.Ticket;
import com.yarin.common_dtos.exceptions.IncompatibilityException;
import com.yarin.common_dtos.order.SeatRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class ScreeningService {
    private final ScreeningClient screeningClient;

    @Autowired
    public ScreeningService(ScreeningClient screeningClient) {
        this.screeningClient = screeningClient;
    }

    public BigDecimal reserveSeatGetPrice(SeatRequest seatRequest){
        ResponseEntity<BigDecimal> seatValidationResponse;
        try {
            seatValidationResponse = screeningClient.reserveSeatGetPrice(seatRequest.screeningId(), seatRequest.seatNumber());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to contact the screening-service", e);
        }

        if (seatValidationResponse.getStatusCode() != HttpStatus.OK || seatValidationResponse.getBody() == null) {
            throw new IncompatibilityException("One of your required seats is either non-existent or not available");
        }

        return seatValidationResponse.getBody();
    }

    public void releaseSeat(Ticket ticket) {
        try {
            screeningClient.cancelSeatReservation(ticket.getScreeningId(), ticket.getSeatNumber());
        } catch (Exception e) {
            log.error("Failed to release seat {} for screening {}", ticket.getSeatNumber(), ticket.getScreeningId(), e);
        }
    }
}
