package com.qmeetx.appointmentservice.application;

import com.qmeetx.appointmentservice.domain.Appointment;
import com.qmeetx.appointmentservice.domain.AppointmentSlot;
import com.qmeetx.appointmentservice.domain.AppointmentStatus;
import com.qmeetx.appointmentservice.infrastructure.AppointmentEventProducer;
import com.qmeetx.appointmentservice.infrastructure.AppointmentRepository;
import com.qmeetx.appointmentservice.infrastructure.AppointmentSlotRepository;
import com.qmeetx.qmeetxshared.events.AppointmentBookedEvent;
import com.qmeetx.qmeetxshared.events.AppointmentCancelledEvent;
import com.qmeetx.qmeetxshared.events.AppointmentCheckedInEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentSlotRepository slotRepository;
    private final AppointmentEventProducer eventProducer;

    public List<AppointmentSlot> getAvailableSlots(Long organizationId, LocalDateTime start, LocalDateTime end) {
        return slotRepository.findByOrganizationIdAndStartTimeBetween(organizationId, start, end);
    }

    @Transactional
    public AppointmentSlot createSlot(AppointmentSlot slot) {
        return slotRepository.save(slot);
    }

    @Transactional
    public Appointment bookAppointment(Long slotId, Long customerId) {
        // 1. Lock and retrieve slot
        AppointmentSlot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        if (slot.isBooked()) {
            throw new IllegalStateException("Slot is already booked");
        }

        // 2. Mark slot as booked
        slot.setBooked(true);
        slotRepository.save(slot);

        // 3. Create appointment
        Appointment appointment = Appointment.builder()
                .slotId(slotId)
                .customerId(customerId)
                .organizationId(slot.getOrganizationId())
                .providerId(slot.getProviderId())
                .appointmentDate(slot.getStartTime())
                .status(AppointmentStatus.BOOKED)
                .build();
        
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // 4. Publish Event
        AppointmentBookedEvent event = AppointmentBookedEvent.builder()
                .appointmentId(savedAppointment.getId())
                .customerId(savedAppointment.getCustomerId())
                .organizationId(savedAppointment.getOrganizationId())
                .slotId(savedAppointment.getSlotId())
                .bookingTime(LocalDateTime.now())
                .build();
        eventProducer.publishAppointmentBooked(event);

        return savedAppointment;
    }

    @Transactional
    public Appointment checkIn(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        
        appointment.setStatus(AppointmentStatus.CHECKED_IN);
        Appointment saved = appointmentRepository.save(appointment);

        // Publish Event to trigger Queue Token creation
        AppointmentCheckedInEvent event = AppointmentCheckedInEvent.builder()
                .appointmentId(saved.getId())
                .customerId(saved.getCustomerId())
                .organizationId(saved.getOrganizationId())
                .checkInTime(LocalDateTime.now())
                .build();
        eventProducer.publishAppointmentCheckedIn(event);

        return saved;
    }
    
    @Transactional
    public void cancelAppointment(Long appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // Free up slot
        AppointmentSlot slot = slotRepository.findById(appointment.getSlotId())
                .orElseThrow(() -> new IllegalStateException("Slot data integrity error"));
        slot.setBooked(false);
        slotRepository.save(slot);

        // Publish Event
        AppointmentCancelledEvent event = AppointmentCancelledEvent.builder()
                .appointmentId(appointment.getId())
                .customerId(appointment.getCustomerId())
                .reason(reason)
                .cancellationTime(LocalDateTime.now())
                .build();
        eventProducer.publishAppointmentCancelled(event);
    }
}
