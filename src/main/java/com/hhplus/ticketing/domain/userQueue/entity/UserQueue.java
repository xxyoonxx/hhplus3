package com.hhplus.ticketing.domain.userQueue.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Data
@Builder
@AllArgsConstructor
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

    public static UserQueue of(long userId, String token, Status status) {
        return new UserQueueBuilder()
                .userId(userId)
                .token(token)
                .status(status)
                .createdDate(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}
