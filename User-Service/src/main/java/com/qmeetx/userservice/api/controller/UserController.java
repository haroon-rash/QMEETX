package com.qmeetx.userservice.api.controller;

import com.qmeetx.userservice.api.dto.ProfileCompletiondto;
import com.qmeetx.userservice.application.UserService.UserProfileComplete;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/profile")

public class UserController
{

private final UserProfileComplete  userProfileComplete;

    public UserController(UserProfileComplete userProfileComplete) {
        this.userProfileComplete = userProfileComplete;
    }


@PostMapping("/complete")
public ResponseEntity<?> completeProfile( @Valid @RequestBody ProfileCompletiondto profileCompletiondto,
// @AuthenticationPrincipal  jwt

// consume Auth I'd from jwt with filter
)
{

   // userProfileComplete.profileCompletion(profileCompletiondto);
}


}
