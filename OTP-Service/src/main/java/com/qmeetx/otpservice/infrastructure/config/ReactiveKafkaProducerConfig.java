package com.qmeetx.otpservice.infrastructure.config;


import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import otp.event.EmailVerifiedEvent;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class ReactiveKafkaProducerConfig {


    @Bean
    public ReactiveKafkaProducerTemplate<String, EmailVerifiedEvent> reactiveKafkaProducerTemplate(KafkaProperties kafkaProperties){
        Map<String, Object> producerProps = kafkaProperties.buildProducerProperties(null);
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(producerProps));
    }



}
