package com.qmeetx.userservice.api.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileCompletiondto {


    private String name;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number")
    private String phone;

    private LocalDate dob;

    private String gender;


}
