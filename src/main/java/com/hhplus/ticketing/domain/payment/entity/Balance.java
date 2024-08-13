package com.hhplus.ticketing.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="balance")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long balanceId;

    private long userId;

    private int balance;

    @OneToMany(mappedBy = "balance")
    private List<BalanceHistory> balanceHistoryEntity = new ArrayList<>();

    @Version
    private int version = 0;

    @Builder
    public Balance(long userId, int balance, int version) {
        this.userId = userId;
        this.balance = balance;
        this.version = version;
    }

    public void chargeBalance(int chargeAmount) {
        this.balance += chargeAmount;
    }

    public void useBalance(int useAmount) {
        this.balance -= useAmount;
    }

}
