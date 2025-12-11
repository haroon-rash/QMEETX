package com.qmeetx.otpservice.application.KafkaProducer;


import com.qmeetx.otpservice.domain.Models.OtpData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import otp.event.EmailVerifiedEvent;
import reactor.core.publisher.Mono;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerifiedProducer {

private final ReactiveKafkaProducerTemplate<String, EmailVerifiedEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.email-verified}")
    private String topic;


    public Mono<Void> sendEmailVerifiedEvent(String  email ,boolean verified) {

        EmailVerifiedEvent event = EmailVerifiedEvent.newBuilder()
                .setEmail(email)
                .setIsVerified(verified)
                .build();

       return kafkaTemplate.send(topic,email, event)
               .doOnSuccess(result->log.info("Event is Successfully Publish at EmailVerifiedEvent {}",event))
               .doOnError(error->log.error("Event Failed at EmailVerifiedProducer class {}",event))
               .then();

    }




}
