package com.cg.siptracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordDTO {
    @NotBlank(message = "OTP cannot be empty")
    @Pattern(regexp = "^[0-9]{6}$",message = "OTP should be of 6 digits")
    private String otp;

    @NotBlank(message = "Password cannot be empty")
    private String newPassword;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;


}
