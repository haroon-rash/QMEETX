package com.qmeetx.queueservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "queue_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tokenNumber;

    @Column(nullable = false)
    private Long queueId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenStatus status;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime servedAt;

    private LocalDateTime completedAt;

    public enum TokenStatus {
        WAITING, SERVING, COMPLETED, CANCELLED
    }
}
