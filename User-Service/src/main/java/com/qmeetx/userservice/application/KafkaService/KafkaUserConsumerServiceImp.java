package com.qmeetx.userservice.application.KafkaService;

import com.qmeetx.qmeetxshared.Topics.KafkaTopics;
import com.qmeetx.userservice.api.dto.UserCreationdto;
import com.qmeetx.userservice.application.mapper.userCreationMapper;
import com.qmeetx.userservice.domain.model.User;
import com.qmeetx.userservice.domain.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaUserConsumerServiceImp implements KafkaUserConsumerService {


    private final UserRepository userRepository;

    public KafkaUserConsumerServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override

    @KafkaListener(
            topics = KafkaTopics.USER_CREATION_EVENT,
            groupId = "${spring.kafka.consumer.group-id}"

    )


    public void consumeUserCreationEvent(UserCreationdto userCreationdto) {

         User user = userCreationMapper.maptoUser(userCreationdto);
        userRepository.save(user);





    }
}
