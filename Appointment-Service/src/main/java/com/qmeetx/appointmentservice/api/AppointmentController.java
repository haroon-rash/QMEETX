package com.qmeetx.appointmentservice.api;

import com.qmeetx.appointmentservice.application.AppointmentService;
import com.qmeetx.appointmentservice.domain.Appointment;
import com.qmeetx.appointmentservice.domain.AppointmentSlot;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/slots")
    public ResponseEntity<List<AppointmentSlot>> getSlots(
            @RequestParam Long organizationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(appointmentService.getAvailableSlots(organizationId, start, end));
    }

    @PostMapping("/slots")
    public ResponseEntity<AppointmentSlot> createSlot(@RequestBody AppointmentSlot slot) {
        return ResponseEntity.ok(appointmentService.createSlot(slot));
    }

    @PostMapping("/book")
    public ResponseEntity<Appointment> bookAppointment(@RequestParam Long slotId, @RequestParam Long customerId) {
        return ResponseEntity.ok(appointmentService.bookAppointment(slotId, customerId));
    }

    @PostMapping("/{id}/check-in")
    public ResponseEntity<Appointment> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.checkIn(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id, @RequestParam String reason) {
        appointmentService.cancelAppointment(id, reason);
        return ResponseEntity.ok().build();
    }
}
