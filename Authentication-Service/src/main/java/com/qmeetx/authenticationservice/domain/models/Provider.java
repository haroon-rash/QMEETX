package com.qmeetx.authenticationservice.domain.models;



import com.qmeetx.authenticationservice.domain.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Provider {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
// GOOGLE, GITHUB, FACEBOOK, etc.
    @Column(name = "Provider_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider providerName;

    // unique user ID given by provider (e.g., Google "sub")
    @Column(name ="Provider_UserId",nullable = false)
    private String providerUserId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
