package com.nt.rookies.assets.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthResponse {

    private String role;
    private Boolean isFirstLogin;
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthResponse(String role, Boolean isFirstLogin, String accessToken) {
        this.role = role;
        this.isFirstLogin = isFirstLogin;
        this.accessToken = accessToken;
    }
}
