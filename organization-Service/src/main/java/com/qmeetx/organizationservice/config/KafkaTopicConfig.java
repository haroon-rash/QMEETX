package com.qmeetx.organizationservice.config;

import com.qmeetx.qmeetxshared.topics.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic organizationEventsTopic() {
        return TopicBuilder.name(KafkaTopics.ORGANIZATION_EVENTS)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
