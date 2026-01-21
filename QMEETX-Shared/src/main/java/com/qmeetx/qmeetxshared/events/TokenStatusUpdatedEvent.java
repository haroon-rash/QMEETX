package com.qmeetx.qmeetxshared.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenStatusUpdatedEvent {
    private Long tokenId;
    private Long queueId;
    private String status;
    private LocalDateTime updatedAt;
}
