package com.qmeetx.apigateway.utils;

import java.io.Serializable;

public record JwtUserPrincipal(String auth_id, String email, String name, String role,
                               Boolean isVerified) implements Serializable {



}
