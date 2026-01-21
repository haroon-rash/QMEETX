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
public class TokenIssuedEvent {
    private Long tokenId;
    private Long queueId;
    private Long tokenNumber;
    private LocalDateTime issuedAt;
}
