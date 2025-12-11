package com.qmeetx.authservice.application.KafkaService;


import com.qmeetx.authservice.api.dto.userCreationDTO;
import com.qmeetx.qmeetxshared.Topics.KafkaTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import userCreation.events.UserCreation;

@Service
public class KafkaUserCreationServiceImp implements KafkaUserCreationService {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaUserCreationServiceImp(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendUserCreationEvent(userCreationDTO user) {

        UserCreation events = UserCreation.newBuilder()
                .setAuthId(user.getAuthId())
                .setName(user.getUsername())
                .setEmail(user.getEmail())
                .setRole(user.getRole())
                .setIsVerified(user.getIsVerified())
                .setSource(user.getSource())
                .build();

        String topic = KafkaTopics.USER_CREATION_EVENT;
        kafkaTemplate.send(topic, user.getAuthId(),events.toByteArray());


    }
}
