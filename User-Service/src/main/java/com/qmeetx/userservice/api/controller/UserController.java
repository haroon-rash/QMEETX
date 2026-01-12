package com.qmeetx.userservice.api.controller;

import com.qmeetx.userservice.api.dto.ProfileCompletiondto;
import com.qmeetx.userservice.application.UserService.UserProfileComplete;
import com.qmeetx.userservice.application.UserService.UserProfileCompletionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/profile")

public class UserController {

    private final UserProfileCompletionService userProfileComplete;

    public UserController(UserProfileComplete userProfileComplete) {
        this.userProfileComplete = userProfileComplete;
    }


    @PatchMapping("/completeprofile")
    public ResponseEntity<?> completeProfile(@Valid @RequestBody ProfileCompletiondto profileCompletiondto, @RequestHeader("X-Auth-Id") UUID authid) {

        userProfileComplete.profileCompletion(authid, profileCompletiondto);

        return ResponseEntity.ok().build();
    }



/*
@PutMapping("/updateprofile")
public ResponseEntity<?> updateProfile(@RequestBody )
}
*/
}