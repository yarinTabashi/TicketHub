package com.yarin.common_dtos.ticket;

import com.yarin.common_dtos.common_dtos.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByScreeningId(Integer screeningId);
    List<Ticket> findByCustomerId(Integer customerId);
}