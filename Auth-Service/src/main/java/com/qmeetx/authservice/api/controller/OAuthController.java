package com.qmeetx.authservice.api.controller;


import com.qmeetx.authservice.application.oauth.Google.OAuthUserService;
import com.qmeetx.authservice.domain.models.User;
import com.qmeetx.authservice.infrastructure.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/oauth/")
@RequiredArgsConstructor
public class OAuthController {

private final HttpServletRequest request;
private final JwtTokenProvider  jwtTokenProvider;

@GetMapping("/success")
    public ResponseEntity<?> success() {

    User user = (User) request.getAttribute("oAuthUser");
    if (user == null) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An Unexpected Error Occurred"));

    }
    try {
        String token = jwtTokenProvider.generateToken(
              user.getId().toString(),
                user.getEmail(),
                user.getName(),
                user.isVarified(),
                user.getRole().toString()
        );

        return ResponseEntity.ok(Map.of("token", token));
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to generate JWT token"));
    }
}

    @GetMapping("/failure")
    public ResponseEntity<Map<String, String>> oauthFailure() {
        return ResponseEntity.status(401).body(Map.of("error", "OAuth login failed"));
    }


}
