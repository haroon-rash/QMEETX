package com.qmeetx.authenticationservice.application.KafkaService;


import com.qmeetx.authenticationservice.api.dto.userCreationDTO;

public interface KafkaUserCreationService {

 public void sendUserCreationEvent(userCreationDTO user);





}
