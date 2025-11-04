package com.github.shoma0xcc.Linkster.auth.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
}
