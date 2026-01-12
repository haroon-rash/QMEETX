package com.qmeetx.authenticationservice.infrastructure.config;


import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfigProducer {


        @Value("${spring.kafka.bootstrap-servers}")
        private String bootstrapServers;



        @Bean
        public ProducerFactory<String, byte[]> producerFactory() {
            Map<String, Object> props = new HashMap<>();

            // Kafka broker address
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

            // Serialize key as String
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);

            // Serialize value as byte array
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.ByteArraySerializer.class);

            // Wait for acknowledgment from all in-sync replicas (highest reliability)
            props.put(ProducerConfig.ACKS_CONFIG, "all");

            // Retry sending message up to 3 times if failed
            props.put(ProducerConfig.RETRIES_CONFIG, 3);

            // Small delay to batch messages (improves throughput)
            props.put(ProducerConfig.LINGER_MS_CONFIG, 5);

            // Enable idempotence to prevent duplicate messages on retries
            props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
            props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4"); // or "snappy"
            props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 240000); // 4 minutes

            return new DefaultKafkaProducerFactory<>(props);
        }



    @Bean
        public KafkaTemplate<String, byte[]> kafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }
    }


