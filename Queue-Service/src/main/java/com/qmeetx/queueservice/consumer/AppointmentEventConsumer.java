package com.qmeetx.queueservice.consumer;

import com.qmeetx.qmeetxshared.events.AppointmentCheckedInEvent;
import com.qmeetx.qmeetxshared.topics.KafkaTopics;
import com.qmeetx.queueservice.application.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentEventConsumer {

    private final QueueService queueService;

    @KafkaListener(topics = KafkaTopics.APPOINTMENT_EVENTS, groupId = "queue-service-group")
    public void consumeAppointmentEvents(byte[] message) {
        try {
            appointment.event.AppointmentEvent wrapper = appointment.event.AppointmentEvent.parseFrom(message);
            
            if (wrapper.hasCheckedIn()) {
                appointment.event.AppointmentCheckedInEvent checkInEvent = wrapper.getCheckedIn();
                log.info("Handling AppointmentCheckedInEvent for appointment {}", checkInEvent.getAppointmentId());
                
                Long queueId = checkInEvent.getQueueId();
                Long organizationId = checkInEvent.getOrganizationId();
                
                if (queueId == 0) {
                    log.info("No Queue ID provided. Looking up queue for organization {}", organizationId);
                    List<com.qmeetx.queueservice.domain.Queue> queues = queueService.getQueuesByOrganization(organizationId);
                    if (!queues.isEmpty()) {
                        queueId = queues.get(0).getId();
                        log.info("Found queue {} for organization {}", queueId, organizationId);
                    }
                }
                
                if (queueId != 0) {
                    try {
                        queueService.joinQueue(queueId);
                        log.info("Successfully auto-joined queue {} for appointment {}", queueId, checkInEvent.getAppointmentId());
                    } catch (Exception e) {
                        log.error("Failed to auto-join queue {} for appointment {}", queueId, checkInEvent.getAppointmentId(), e);
                    }
                } else {
                    log.warn("No suitable queue found for organization {}. Skipping auto-join.", organizationId);
                }
            } else {
                log.info("Ignored non-CheckedIn event.");
            }
        } catch (Exception e) {
            log.error("Failed to parse or process event: {}", e.getMessage());
        }
    }
}
