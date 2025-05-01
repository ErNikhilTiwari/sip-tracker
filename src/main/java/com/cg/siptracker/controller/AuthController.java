package com.cg.siptracker.controller;

import com.cg.siptracker.dto.*;
import com.cg.siptracker.model.User;
import com.cg.siptracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody RegisterDTO registerDTO) {
        ResponseDTO response = userService.registerUser(registerDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> loginUser(@RequestBody LoginDTO loginDTO) {
        ResponseDTO response = userService.loginUser(loginDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestBody UserDTO forgetDTO) {
        ResponseDTO response = userService.forgotPassword(forgetDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        ResponseDTO response = userService.resetPassword(resetPasswordDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
