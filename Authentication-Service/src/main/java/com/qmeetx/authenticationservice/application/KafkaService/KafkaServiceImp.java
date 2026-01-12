package com.qmeetx.authenticationservice.application.KafkaService;

import auth.events.OtpEvent;

import com.qmeetx.qmeetxshared.Topics.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaServiceImp implements KafkaProducerService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;



    private final String evenTopic="Otp_Event";
    public KafkaServiceImp(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }


    @Override
    public void sendOTPEvent( String name, String email) {

        OtpEvent otpEvent = OtpEvent.newBuilder()

                .setName(name)
                .setEmail(email)
                .build();
        kafkaTemplate.send(evenTopic,email,otpEvent.toByteArray());
log.info("Sending OTP event {SIGNUP} to topic  {}",evenTopic);

    }
}
