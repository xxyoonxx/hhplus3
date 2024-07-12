package com.hhplus.ticketing.domain.queue.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name="user_queue")
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tokenId;

    private long userId;

    private String token;

    private Status status;

    private LocalDateTime createdDate;

    private LocalDateTime expiryDate;

    public enum Status {
        WAITING, PROCESSING, EXPIRED
    }

    @Builder
    public Queue(long tokenId, long userId, String token, Status status, LocalDateTime createdDate, LocalDateTime expiryDate) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.token = token;
        this.status = status;
        this.createdDate = createdDate;
        this.expiryDate = expiryDate;
    }

    public void expire() {
        this.status = Status.EXPIRED;
    }
}
