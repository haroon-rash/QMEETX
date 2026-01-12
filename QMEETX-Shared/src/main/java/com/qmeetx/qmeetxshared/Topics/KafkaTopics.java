package com.qmeetx.qmeetxshared.Topics;


public final class KafkaTopics {


    private KafkaTopics() {} // Prevent object creation

    // Auth → User Service
    public static String MAIL_VERIFIED_EVENT="email-verified-topic";
    public static final String USER_CREATION_EVENT = "user-creation-event-topic";

    public static final String OTP_EVENT="Otp_Event";
}

