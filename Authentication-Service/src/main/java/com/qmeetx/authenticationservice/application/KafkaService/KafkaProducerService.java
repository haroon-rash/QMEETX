package com.qmeetx.authenticationservice.application.KafkaService;


public interface KafkaProducerService {

public void sendOTPEvent(String name,String email);


}
