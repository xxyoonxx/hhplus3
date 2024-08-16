package com.hhplus.ticketing.domain.outbox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@Table(name="outbox")
@NoArgsConstructor
@AllArgsConstructor
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboxId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Domain domain;

    private String message;

    private LocalDateTime createdDate;

    public enum Type {
        INIT, PUBLISHED
    }

    public enum Domain {
        RESERVATION, USERQUEUE
    }

    public Outbox changeTypeToPublished() {
        this.type = Type.PUBLISHED;
        return this;
    }

    public static Outbox of(Type type, Domain domain, String message) {
        return Outbox.builder()
                .type(type)
                .domain(domain)
                .message(message)
                .createdDate(LocalDateTime.now())
                .build();
    }

}
