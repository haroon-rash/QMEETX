package com.qmeetx.queueservice.infrastructure;

import com.qmeetx.qmeetxshared.topics.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueEventProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void publishEvent(String topic, byte[] event) {
        try {
            kafkaTemplate.send(topic, event);
            log.info("Published event to topic {}: size {}", topic, event.length);
        } catch (Exception e) {
            log.error("Error publishing event to topic {}: {}", topic, e.getMessage());
        }
    }

    public void publishQueueCreated(com.qmeetx.qmeetxshared.events.QueueCreatedEvent event) {
        queue.events.QueueCreatedEvent proto = queue.events.QueueCreatedEvent.newBuilder()
                .setQueueId(event.getQueueId() != null ? event.getQueueId() : 0)
                .setName(event.getName() != null ? event.getName() : "")
                .setOrganizationId(event.getOrganizationId() != null ? event.getOrganizationId() : 0)
                .setCreatedAt(event.getCreatedAt() != null ? event.getCreatedAt().toString() : "")
                .build();
        
        queue.events.QueueEvent wrapper = queue.events.QueueEvent.newBuilder()
                .setQueueCreated(proto)
                .build();
        publishEvent(KafkaTopics.QUEUE_EVENTS, wrapper.toByteArray());
    }
    
    public void publishTokenIssued(com.qmeetx.qmeetxshared.events.TokenIssuedEvent event) {
        queue.events.TokenIssuedEvent proto = queue.events.TokenIssuedEvent.newBuilder()
                .setTokenId(event.getTokenId() != null ? event.getTokenId() : 0)
                .setQueueId(event.getQueueId() != null ? event.getQueueId() : 0)
                .setTokenNumber(event.getTokenNumber() != null ? event.getTokenNumber() : 0)
                .setIssuedAt(event.getIssuedAt() != null ? event.getIssuedAt().toString() : "")
                .build();

        queue.events.QueueEvent wrapper = queue.events.QueueEvent.newBuilder()
                .setTokenIssued(proto)
                .build();
        publishEvent(KafkaTopics.QUEUE_EVENTS, wrapper.toByteArray());
    }

    public void publishTokenStatusUpdated(com.qmeetx.qmeetxshared.events.TokenStatusUpdatedEvent event) {
        queue.events.TokenStatusUpdatedEvent proto = queue.events.TokenStatusUpdatedEvent.newBuilder()
                .setTokenId(event.getTokenId() != null ? event.getTokenId() : 0)
                .setQueueId(event.getQueueId() != null ? event.getQueueId() : 0)
                .setStatus(event.getStatus() != null ? event.getStatus() : "")
                .setUpdatedAt(event.getUpdatedAt() != null ? event.getUpdatedAt().toString() : "")
                .build();

        queue.events.QueueEvent wrapper = queue.events.QueueEvent.newBuilder()
                .setTokenStatusUpdated(proto)
                .build();
        publishEvent(KafkaTopics.QUEUE_EVENTS, wrapper.toByteArray());
    }
}
