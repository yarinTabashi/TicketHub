package com.yarin.common_dtos.order;
import com.yarin.common_dtos.common_dtos.Ticket;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "order_table")
public class Order {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true,  nullable = false)
    private String orderCode;

    @Positive
    private BigDecimal totalAmount;

    private String customerId;

    @Enumerated(EnumType.STRING) // This will store the status as a String in the DB
    @Column(nullable = false)
    private OrderStatus status; // (PENDING, SUCCESS, ...)

    private List<Ticket> tickets;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime bookingDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
}