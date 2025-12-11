package com.qmeetx.userservice.api.dto;

import lombok.Data;

@Data
public class UserCreationdto {

    private   String authId;
    private String username;
    private String email;
    private String role;
    private Boolean isVerified;
    private String source;


}
