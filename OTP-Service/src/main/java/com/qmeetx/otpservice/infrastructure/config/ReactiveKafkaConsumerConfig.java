package com.qmeetx.otpservice.infrastructure.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ReactiveKafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.topic.otp-event}")
    private String topic;

    @Bean
    public ReceiverOptions<String, byte[]> kafkaReceiverOptions() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return ReceiverOptions.<String, byte[]>create(props)
                .subscription(java.util.Collections.singleton(topic));
    }

    @Bean
    public Flux<byte[]> kafkaMessageFlux(ReceiverOptions<String, byte[]> receiverOptions) {
        return KafkaReceiver.create(receiverOptions)
                .receive()
                .map(record -> {
                    byte[] value = record.value();
                    record.receiverOffset().acknowledge(); // Commit offset reactively
                    return value;
                })
                .doOnError(e -> System.err.println("Kafka error: " + e.getMessage()));
    }
}
