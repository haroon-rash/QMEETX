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
public class AppointmentCancelledEvent {
    private Long appointmentId;
    private Long customerId;
    private String reason;
    private LocalDateTime cancellationTime;
}
