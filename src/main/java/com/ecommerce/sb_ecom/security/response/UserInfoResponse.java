package com.ecommerce.sb_ecom.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
//@AllArgsConstructor
public class UserInfoResponse {

    private String id;
    private String jwtToken;
    private String username;
    private List<String> roles;


    public UserInfoResponse(String id, String jwtToken, String username, List<String> roles) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
    }

    public UserInfoResponse(String id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
