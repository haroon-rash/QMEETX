package com.qmeetx.appointmentservice.infrastructure;

import com.qmeetx.qmeetxshared.events.AppointmentBookedEvent;
import com.qmeetx.qmeetxshared.events.AppointmentCancelledEvent;
import com.qmeetx.qmeetxshared.events.AppointmentCheckedInEvent;
import com.qmeetx.qmeetxshared.topics.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentEventProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void publishAppointmentBooked(com.qmeetx.qmeetxshared.events.AppointmentBookedEvent event) {
        log.info("Publishing AppointmentBookedEvent: {}", event);
        appointment.event.AppointmentBookedEvent booked = appointment.event.AppointmentBookedEvent.newBuilder()
                .setAppointmentId(event.getAppointmentId() != null ? event.getAppointmentId() : 0)
                .setCustomerId(event.getCustomerId() != null ? event.getCustomerId() : 0)
                .setSlotId(event.getSlotId() != null ? event.getSlotId() : 0)
                .setOrganizationId(event.getOrganizationId() != null ? event.getOrganizationId() : 0)
                .setBookingTime(event.getBookingTime() != null ? event.getBookingTime().toString() : "")
                .build();

        appointment.event.AppointmentEvent wrapper = appointment.event.AppointmentEvent.newBuilder()
                .setBooked(booked)
                .build();
        kafkaTemplate.send(KafkaTopics.APPOINTMENT_EVENTS, wrapper.toByteArray());
    }

    public void publishAppointmentCancelled(com.qmeetx.qmeetxshared.events.AppointmentCancelledEvent event) {
        log.info("Publishing AppointmentCancelledEvent: {}", event);
        appointment.event.AppointmentCancelledEvent cancelled = appointment.event.AppointmentCancelledEvent.newBuilder()
                .setAppointmentId(event.getAppointmentId() != null ? event.getAppointmentId() : 0)
                .setCustomerId(event.getCustomerId() != null ? event.getCustomerId() : 0)
                .setReason(event.getReason() != null ? event.getReason() : "")
                .setCancellationTime(event.getCancellationTime() != null ? event.getCancellationTime().toString() : "")
                .build();

        appointment.event.AppointmentEvent wrapper = appointment.event.AppointmentEvent.newBuilder()
                .setCancelled(cancelled)
                .build();
        kafkaTemplate.send(KafkaTopics.APPOINTMENT_EVENTS, wrapper.toByteArray());
    }

    public void publishAppointmentCheckedIn(com.qmeetx.qmeetxshared.events.AppointmentCheckedInEvent event) {
        log.info("Publishing AppointmentCheckedInEvent: {}", event);
        appointment.event.AppointmentCheckedInEvent checkedIn = appointment.event.AppointmentCheckedInEvent.newBuilder()
                .setAppointmentId(event.getAppointmentId() != null ? event.getAppointmentId() : 0)
                .setCustomerId(event.getCustomerId() != null ? event.getCustomerId() : 0)
                .setOrganizationId(event.getOrganizationId() != null ? event.getOrganizationId() : 0)
                .setQueueId(event.getQueueId() != null ? event.getQueueId() : 0)
                .setCheckInTime(event.getCheckInTime() != null ? event.getCheckInTime().toString() : "")
                .build();

        appointment.event.AppointmentEvent wrapper = appointment.event.AppointmentEvent.newBuilder()
                .setCheckedIn(checkedIn)
                .build();
        kafkaTemplate.send(KafkaTopics.APPOINTMENT_EVENTS, wrapper.toByteArray());
    }
}
