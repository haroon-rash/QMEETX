package com.qmeetx.userservice.application.KafkaService;

import com.qmeetx.qmeetxshared.topics.KafkaTopics;
import com.qmeetx.userservice.api.dto.UserCreationdto;
import com.qmeetx.userservice.application.mapper.userCreationMapper;
import com.qmeetx.userservice.domain.model.User;
import com.qmeetx.userservice.domain.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;

@lombok.extern.slf4j.Slf4j
@org.springframework.stereotype.Service
public class KafkaUserConsumerServiceImp implements KafkaUserConsumerService {


    private final UserRepository userRepository;

    public KafkaUserConsumerServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override

    @KafkaListener(topics = KafkaTopics.USER_CREATION_EVENT, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUserCreationEvent(byte[] message) {
        log.info("Received UserCreation event message of size: {}", message.length);
        try {
            userCreation.events.UserCreation event = userCreation.events.UserCreation.parseFrom(message);
            UserCreationdto userCreationdto = new UserCreationdto();
            userCreationdto.setAuthId(event.getAuthId());
            userCreationdto.setUsername(event.getName());
            userCreationdto.setEmail(event.getEmail());
            userCreationdto.setRole(event.getRole());
            userCreationdto.setIsVerified(event.getIsVerified());
            userCreationdto.setSource(event.getSource());

            User user = userCreationMapper.maptoUser(userCreationdto);
            userRepository.save(user);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw new RuntimeException("Error parsing UserCreation event", e);
        }
    }
}
