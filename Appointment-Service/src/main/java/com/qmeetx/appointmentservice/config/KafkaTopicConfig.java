package com.qmeetx.appointmentservice.config;

import com.qmeetx.qmeetxshared.topics.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic appointmentEventsTopic() {
        return TopicBuilder.name(KafkaTopics.APPOINTMENT_EVENTS)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
