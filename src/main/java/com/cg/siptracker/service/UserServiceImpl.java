package com.cg.siptracker.service;

import com.cg.siptracker.dto.*;
import com.cg.siptracker.model.User;
import com.cg.siptracker.repository.UserRepository;
import com.cg.siptracker.utility.JwtUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

import java.util.Optional;

@Slf4j // Lombok Logging
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtUtility jwtUtility;

    private static final SecureRandom random = new SecureRandom();
    @Override
    public ResponseDTO registerUser(RegisterDTO registerDTO) {
        log.info("Registering user: {}", registerDTO.getEmail());
        ResponseDTO res = new ResponseDTO("User already exist",null);
        if (existsByEmail(registerDTO.getEmail())) {
            log.warn("Registration failed: User already exists with email {}", registerDTO.getEmail());
            res.setMessage("error");
            res.setData("User Already Exists");
            return res;
        }

        User user = new User();
        user.setFullName(registerDTO.getFullName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        log.info("User {} registered successfully", user.getEmail());
        emailService.sendEmail(user.getEmail(), "Registered in SIP Tracker", "Hi....\n You have been successfully registered!");

        res.setMessage("User Registered Successfully");
        res.setData("Full name:" +user.getFullName() +"\n" +"Email: "+user.getEmail()  );
        return res;
    }

    @Override
    public ResponseDTO loginUser(LoginDTO loginDTO) {
        log.info("Login attempt for user: {}", loginDTO.getEmail());
        ResponseDTO res = new ResponseDTO();
        Optional<User> userExists = getUserByEmail(loginDTO.getEmail());


        if (userExists.isPresent()) {
            User user = userExists.get();
            if (matchPassword(loginDTO.getPassword(), user.getPassword())) {
                String token = jwtUtility.generateToken(user.getEmail());
//                user.setToken(token);
//                userRepository.save(user);

                log.debug("Login successful for user: {} - Token generated", user.getEmail());
                emailService.sendEmail(user.getEmail(), "Logged in SIP Tracker", "Hi....\n You have been successfully logged in! " + token);
                res.setMessage("User Logged In Successfully: ");
                res.setData("Token: " + token);
                return res;
            } else {
                log.warn("Invalid credentials for user: {}", loginDTO.getEmail());
                res.setMessage("error");
                res.setData("Invalid Credentials");
                return res;
            }
        }

        log.error("User not found with email: {}", loginDTO.getEmail());
        res.setMessage("error");
        res.setData("User Not Found");
        return res;
    }

    @Override
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        log.debug("Matching password for login attempt");
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking if user exists by email: {}", email);
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public ResponseDTO forgotPassword(UserDTO forgotPasswordDTO) {
        Optional<User> userOptional = userRepository.findByEmail(forgotPasswordDTO.getEmail());
        if (userOptional.isEmpty()) {
            return new ResponseDTO("error", "User not found with this email");
        }

        User user = userOptional.get();
        String otp = String.valueOf(100000 + random.nextInt(900000));
        user.setResetOTP(otp);
        userRepository.save(user);

        emailService.sendEmail(forgotPasswordDTO.getEmail(), "Reset Password", "OTP to reset your password: " + otp);

        return new ResponseDTO("Reset OTP Sent", otp);
    }

    public ResponseDTO resetPassword(ResetPasswordDTO resetPasswordDTO){
        Optional<User> userOptional = userRepository.findByEmail(resetPasswordDTO.getEmail());
        if (userOptional.isEmpty()) {
            return new ResponseDTO("error", "User not found with this email");
        }

        User user = userOptional.get();
        String resetOTP =user.getResetOTP();
        if(resetPasswordDTO.getOtp().equals(resetOTP)){
            user.setPassword(resetPasswordDTO.getNewPassword());
            user.setResetOTP(null);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println(resetPasswordDTO.getNewPassword());
            userRepository.save(user);
            emailService.sendEmail(resetPasswordDTO.getEmail(), "Password Updated","password has been changed and token expired");
            return new ResponseDTO("Password Updated", "password has been changed and token expired");
        }
        else{
            return new ResponseDTO("token not valid","verify your link again");
        }

    }

}