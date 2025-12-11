package com.qmeetx.otpservice.domain.controller;


import com.qmeetx.otpservice.application.otpService.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class OtpController {
    private final OtpService otpService;
    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    public static record OtpValidationRequest(String token , String otpCode){}

    @PostMapping("/verify-otp")
    public Mono<ResponseEntity<String>> verifyOtp(@RequestBody OtpValidationRequest request) {
        String token = request.token();
        String otpCode = request.otpCode();

        return otpService.verifyOtp(token, otpCode)
                .map(success -> {
                    if (success) {
                        return ResponseEntity.ok("OTP verified successfully");
                    }
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Verification failed. Please try again with the correct OTP.");
                })
                .switchIfEmpty(Mono.just(
                        ResponseEntity.status(HttpStatus.GONE) // 410 Gone = expired
                                .body("OTP expired or not found")
                ));
    }




}
