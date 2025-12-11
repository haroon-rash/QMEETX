package com.qmeetx.otpservice.application.MailService;

import com.qmeetx.otpservice.domain.Models.OtpData;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class MailServiceImp implements MailService {

    private final JavaMailSender mailSender;

    public MailServiceImp(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public Mono<Void> sendOtpMail(OtpData otpData) {
        return Mono.fromRunnable(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(otpData.getEmail());
                helper.setSubject("Your OTP Code");
                helper.setText("Hello " + otpData.getName() + ",\n\nYour OTP Code is:  "
                        + otpData.getOtpCode() + "\n\n It expires in 5 minutes. Please don't share your code");

                mailSender.send(message);
                log.info("OTP sent to {}", otpData.getEmail());
            } catch (MessagingException e) {
                log.error("Failed to send OTP email to {}", otpData.getEmail(), e);
                throw new RuntimeException(e);
            }
        });
    }
}
