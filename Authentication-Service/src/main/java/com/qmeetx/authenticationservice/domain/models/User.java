package com.qmeetx.authenticationservice.domain.models;


import com.qmeetx.authenticationservice.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Signup_User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id",nullable = false)
    private UUID id;

    @Column(name = "user_name",nullable = false)
    private String name;

    @Column(name="user_email",unique = true,nullable = false)
    private String email;

    @Column(name="user_password")
    private String password;


    private boolean isVarified=false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private UserRole role;



    // 🔑 one user can have multiple providers
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Provider> providers = new HashSet<>();
}


  /*


  /*  @Column(name = "user_phoneNo",nullable = false)
    private String phone;
*/
   /* @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
    @Column(name="user_industry",nullable = false)
    private String industry;
*/






