package com.qmeetx.qmeetxshared.topics;


public final class KafkaTopics {


    private KafkaTopics() {} // Prevent object creation

    // Auth → User Service
    public static final String MAIL_VERIFIED_EVENT="email-verified-topic";
    public static final String USER_CREATION_EVENT = "user-creation-event-topic";

    public static final String OTP_EVENT="Otp_Event";

    // Queue Service
    public static final String QUEUE_EVENTS = "queue-events-topic";
    
    // Organization Service
    public static final String ORGANIZATION_EVENTS = "organization-events-topic";

    // Appointment Service
    public static final String APPOINTMENT_EVENTS = "appointment-events-topic";
}

