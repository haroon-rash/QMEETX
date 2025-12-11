package com.qmeetx.userservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

@Configuration
public class KafkaRetryConfig {




        @Bean
        public DefaultErrorHandler errorHandler(KafkaTemplate<?, ?> template) {
            var backOff = new ExponentialBackOffWithMaxRetries(3);
            backOff.setInitialInterval(2000L);
            backOff.setMultiplier(2.0);
            backOff.setMaxInterval(10000L);

            return new DefaultErrorHandler(
                    new DeadLetterPublishingRecoverer(template), backOff);
        }
    }


