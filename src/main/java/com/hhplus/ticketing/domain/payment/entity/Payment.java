package com.hhplus.ticketing.domain.payment.entity;

import com.hhplus.ticketing.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="reservation_pay")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id")
    private Reservation reservation;

    private LocalDateTime payDate;

    private long payAmount;

    @Enumerated(EnumType.STRING)
    private Status status;

    public static Payment of(Reservation reservation, long payAmount, Status status) {
        return new PaymentBuilder()
                .payAmount(payAmount)
                .status(status)
                .payDate(LocalDateTime.now())
                .reservation(reservation)
                .build();
    }

    public enum Status {
        WAITING, DONE, EXPIRED
    }

    public Payment changeStatus(Status status) {
        this.status = status;
        return this;
    }

    public Payment changeStatusToDone() {
        this.status = Status.DONE;
        return this;
    }

    public Payment changeStatusToExpired() {
        this.status = Status.EXPIRED;
        return this;
    }

}
