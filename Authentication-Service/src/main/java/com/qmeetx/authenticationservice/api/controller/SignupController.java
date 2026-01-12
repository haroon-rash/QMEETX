package com.qmeetx.authenticationservice.api.controller;


import com.qmeetx.authenticationservice.api.dto.SignupRequestDTO;
import com.qmeetx.authenticationservice.application.signupService.SignupService;
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
