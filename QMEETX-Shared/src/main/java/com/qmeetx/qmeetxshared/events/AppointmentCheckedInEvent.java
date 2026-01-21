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
public class AppointmentCheckedInEvent {
    private Long appointmentId;
    private Long customerId;
    private Long organizationId;
    private Long queueId; // Optional: If we map service to queue
    private LocalDateTime checkInTime;
}
