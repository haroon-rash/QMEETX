package com.qmeetx.authservice.api.controller;

import com.qmeetx.authservice.api.dto.LoginRequestDTO;
import com.qmeetx.authservice.application.loginService.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @PostMapping("/login")
    public ResponseEntity<?>login (@RequestBody LoginRequestDTO loginRequestDTO) {

        String token =loginService.ValidateLogin(loginRequestDTO);

       return ResponseEntity.ok(
               Map.of("token",token
               , "message","login Successfully"
       ));
    }



}
