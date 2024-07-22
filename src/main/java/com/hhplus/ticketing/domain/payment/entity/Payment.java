package com.hhplus.ticketing.domain.payment.entity;

import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name="reservation_pay")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id")
    private Reservation reservation;

    private LocalDateTime payDate;

    private long payAmount;

    private Status status;

    @Builder
    public Payment(Long payId, Reservation reservation, LocalDateTime payDate, long payAmount, Status status) {
        this.payId = payId;
        this.reservation = reservation;
        this.payDate = payDate;
        this.payAmount = payAmount;
        this.status = status;
    }

    public enum Status {
        WAITING, DONE, EXPIRED
    }

    public Payment changeStatus(Status status) {
        this.status = status;
        return this;
    }

}
