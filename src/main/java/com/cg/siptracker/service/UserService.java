package com.cg.siptracker.service;

import com.cg.siptracker.dto.*;
import com.cg.siptracker.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    ResponseDTO registerUser(RegisterDTO registerDTO);
    ResponseDTO loginUser(LoginDTO loginDTO);
    ResponseDTO forgotPassword(RegisterDTO request);
    ResponseDTO resetPassword(ResetPasswordDTO request);
    ResponseDTO changePassword(ChangePasswordDTO request, String token);

}