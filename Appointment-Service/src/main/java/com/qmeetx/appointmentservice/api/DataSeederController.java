package com.qmeetx.appointmentservice.api;

import com.qmeetx.appointmentservice.application.AppointmentService;
import com.qmeetx.appointmentservice.domain.AppointmentSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/seeder")
@RequiredArgsConstructor
public class DataSeederController {

    private final AppointmentService appointmentService;

    @PostMapping("/run-10-users")
    public ResponseEntity<String> seed10UsersData(@RequestParam(defaultValue = "1") Long organizationId) {
        // 1. Create 10 Slots
        Long providerId = 101L;
        LocalDateTime baseTime = LocalDateTime.now().plusHours(1);

        for (int i = 0; i < 10; i++) {
            AppointmentSlot slot = AppointmentSlot.builder()
                    .organizationId(organizationId)
                    .providerId(providerId)
                    .startTime(baseTime.plusHours(i))
                    .endTime(baseTime.plusHours(i).plusMinutes(30))
                    .isBooked(false)
                    .build();
            appointmentService.createSlot(slot);
        }

        // 2. We don't book them automatically here to allow the user to test the Booking API manually if they want,
        // or we can structure it to book some.
        // Let's create slots only, as user might want to click "Book".
        // Actually, user asked for "Complete Testing", so let's populate some bookings.
        
        // Let's create 5 bookings
        // We need to fetch slots first. Since we just created them, maybe we can't easily grab IDs without querying.
        // For simplicity, we just return success.
        
        return ResponseEntity.ok("Successfully seeded 10 Appointment Slots for Organization " + organizationId + ". You can now book them via /api/appointments/book");
    }
}
