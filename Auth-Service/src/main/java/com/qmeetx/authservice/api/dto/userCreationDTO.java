package com.qmeetx.authservice.api.dto;

import com.qmeetx.authservice.domain.enums.EventSource;
import lombok.Data;

@Data
public class userCreationDTO {

    private   String authId;
    private String username;
    private String email;
    private String role;
    private Boolean isVerified;
    private String source= EventSource.Auth_Service.name();


}
