package com.cg.siptracker.service;

import com.cg.siptracker.dto.*;
import com.cg.siptracker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResponseDTO registerUser(RegisterDTO registerDTO);

    ResponseDTO loginUser(LoginDTO loginDTO);

    boolean matchPassword(String rawPassword, String encodedPassword);

    boolean existsByEmail(String email);

    Optional<User> getUserByEmail(String email);
    ResponseDTO forgotPassword(UserDTO forgotPasswordDTO);
    ResponseDTO resetPassword(ResetPasswordDTO resetPasswordDTO);

//    List<SipDTO> getUserSips(Long userId);
}