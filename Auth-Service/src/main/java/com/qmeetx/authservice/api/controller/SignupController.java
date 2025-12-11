package com.qmeetx.authservice.api.controller;

import com.qmeetx.authservice.api.dto.SignupRequestDTO;
import com.qmeetx.authservice.application.signupService.SignupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class SignupController {

    private final SignupService signupservice;

    public SignupController(SignupService signupservice) {
        this.signupservice = signupservice;
    }


    @PostMapping("/signup")
public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO) {

signupservice.signup(signupRequestDTO);
return ResponseEntity.ok().body("Signup Successful");
    }




}
