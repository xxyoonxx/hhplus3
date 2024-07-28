package com.hhplus.ticketing.domain.userQueue.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Data
@NoArgsConstructor
@Table(name="user_queue")
public class UserQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queueId;

    private Long userId;

    private String token;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdDate;

    private LocalDateTime expiryDate;

    public enum Status {
        WAITING, PROCESSING, EXPIRED
    }

    @Builder
    public UserQueue(long queueId, long userId, String token, Status status, LocalDateTime createdDate, LocalDateTime expiryDate) {
        this.queueId = queueId;
        this.userId = userId;
        this.token = token;
        this.status = status;
        this.createdDate = createdDate;
        this.expiryDate = expiryDate;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}
