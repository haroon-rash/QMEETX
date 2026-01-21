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
public class QueueCreatedEvent {
    private Long queueId;
    private String name;
    private Long organizationId;
    private LocalDateTime createdAt;
}
