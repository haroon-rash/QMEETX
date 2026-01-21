package com.qmeetx.realtimeservice.consumer;

import com.qmeetx.qmeetxshared.events.QueueCreatedEvent;
import com.qmeetx.qmeetxshared.events.TokenIssuedEvent;
import com.qmeetx.qmeetxshared.events.TokenStatusUpdatedEvent;
import com.qmeetx.qmeetxshared.topics.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueEventConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = KafkaTopics.QUEUE_EVENTS, groupId = "realtime-service-group")
    public void consumeQueueEvents(byte[] message) {
        try {
            queue.events.QueueEvent wrapper = queue.events.QueueEvent.parseFrom(message);
            
            if (wrapper.hasTokenIssued()) {
                queue.events.TokenIssuedEvent event = wrapper.getTokenIssued();
                // Map Proto to POJO if needed, or send Proto?
                // The messagingTemplate presumably sends JSON to frontend via WebSocket.
                // It's safer to map to the POJO or DTO expected by frontend.
                // Assuming existing POJOs are what frontend expects.
                com.qmeetx.qmeetxshared.events.TokenIssuedEvent pojo = com.qmeetx.qmeetxshared.events.TokenIssuedEvent.builder()
                        .tokenId(event.getTokenId())
                        .queueId(event.getQueueId())
                        .tokenNumber(event.getTokenNumber())
                        .issuedAt(parseDateTime(event.getIssuedAt()))
                        .build();
                messagingTemplate.convertAndSend("/topic/queue/" + pojo.getQueueId(), pojo);
                
            } else if (wrapper.hasTokenStatusUpdated()) {
                queue.events.TokenStatusUpdatedEvent event = wrapper.getTokenStatusUpdated();
                com.qmeetx.qmeetxshared.events.TokenStatusUpdatedEvent pojo = com.qmeetx.qmeetxshared.events.TokenStatusUpdatedEvent.builder()
                        .tokenId(event.getTokenId())
                        .queueId(event.getQueueId())
                        .status(event.getStatus())
                        .updatedAt(parseDateTime(event.getUpdatedAt()))
                        .build();
                messagingTemplate.convertAndSend("/topic/queue/" + pojo.getQueueId(), pojo);
                
            } else if (wrapper.hasQueueCreated()) {
                queue.events.QueueCreatedEvent event = wrapper.getQueueCreated();
                com.qmeetx.qmeetxshared.events.QueueCreatedEvent pojo = com.qmeetx.qmeetxshared.events.QueueCreatedEvent.builder()
                        .queueId(event.getQueueId())
                        .name(event.getName())
                        .organizationId(event.getOrganizationId())
                        .createdAt(parseDateTime(event.getCreatedAt()))
                        .build();
                messagingTemplate.convertAndSend("/topic/org/" + pojo.getOrganizationId(), pojo);
            }
        } catch (Exception e) {
            log.error("Error processing queue event", e);
        }
    }
    
    private java.time.LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) return null;
        try {
            return java.time.LocalDateTime.parse(dateTimeStr);
        } catch (Exception e) {
            return null; // or verification logic
        }
    }
}
