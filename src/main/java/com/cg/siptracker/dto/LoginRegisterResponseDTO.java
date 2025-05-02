package com.cg.siptracker.dto;

import lombok.Data;

@Data
public class LoginRegisterResponseDTO {
    private String fullName;
    private String email;
    private String token;

    public LoginRegisterResponseDTO(String fullName, String email, String token) {
        this.fullName = fullName;
        this.email = email;
        this.token = token;
    }

    public LoginRegisterResponseDTO(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }
}
