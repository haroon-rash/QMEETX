package com.qmeetx.organizationservice.infrastructure;

import com.qmeetx.qmeetxshared.topics.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationEventProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void publishOrganizationCreated(com.qmeetx.qmeetxshared.events.OrganizationCreatedEvent event) {
        try {
            organization.event.OrganizationCreatedEvent proto = organization.event.OrganizationCreatedEvent.newBuilder()
                    .setOrganizationId(event.getOrganizationId() != null ? event.getOrganizationId() : 0)
                    .setName(event.getName() != null ? event.getName() : "")
                    .setCode(event.getCode() != null ? event.getCode() : "")
                    .setCreatedAt(event.getCreatedAt() != null ? event.getCreatedAt().toString() : "")
                    .build();
            
            kafkaTemplate.send(KafkaTopics.ORGANIZATION_EVENTS, proto.toByteArray());
            log.info("Published organization created event: {}", event);
        } catch (Exception e) {
            log.error("Error publishing organization created event: {}", e.getMessage());
        }
    }
}
