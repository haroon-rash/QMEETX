package com.qmeetx.userservice.domain.model;
import com.qmeetx.userservice.domain.Enums.Source;
import com.qmeetx.userservice.domain.Enums.UserRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.UUID;

@Entity

@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
@GeneratedValue(strategy = GenerationType.AUTO)
private UUID id;

    @Column(name = "user_name",nullable = false)
    private String name;

     @Column(name="auth_id",nullable = false)
     private String authId;


    @Column(name="user_email",unique = true,nullable = false)
    private String email;



    @Column(name = "isVerified")
    private Boolean isVerified=false;
    @CreatedDate
    @Column(name = "CreatedAt",updatable = false,insertable = true,nullable = false)
    private LocalDateTime createdAt;

    @Column(name="Role",nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoles Role;
    @Column(name = "source")
    @Enumerated(EnumType.STRING)
    private Source Source  ;
    @LastModifiedDate
    private LocalDateTime updatedAt;




    @Column(name = "profile_completed")
    private Boolean profileCompleted = false;

    @Column(name = "phone_no")
    private String phone;

    private String address;
    private String city;
    private String postalCode;
    private String country;
    @Column(name = "date_of_birth")
    private LocalDate dob;
    private String gender;



}
