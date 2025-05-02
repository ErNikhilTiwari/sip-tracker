package com.cg.siptracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String fullName;
    private String email;
    private String token;
}
