package com.qmeetx.userservice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileCompletiondto {

    @NotBlank(message = "Phone Number is Required!")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number")
    private String phone;
    @NotNull(message = "Date of Birth is Required!")
    private LocalDate dob;
    @NotBlank(message = "Gender  is Required! ")
    private String gender;


}
