package com.hhplus.ticketing.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name="balance_history")
public class BalanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long balanceHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="balanceId")
    private Balance balance;

    private int amount;

    @Enumerated(EnumType.STRING)
    private Type type;

    private LocalDateTime createdDate;

    @Builder
    public BalanceHistory(Balance balance, int amount, Type type) {
        this.balance = balance;
        this.amount = amount;
        this.type = type;
        this.createdDate = LocalDateTime.now();
    }

    public enum Type {
        CHARGE,
        USE
    }

}
