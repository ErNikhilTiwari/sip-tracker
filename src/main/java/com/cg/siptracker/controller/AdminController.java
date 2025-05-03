package com.cg.siptracker.controller;

import com.cg.siptracker.dto.ResponseDTO;
import com.cg.siptracker.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // 1. Get all users
    @GetMapping("/users")
    public ResponseEntity<ResponseDTO> getAllUsers(@RequestHeader("Authorization") String token) {
        log.info("Admin fetching all users...");
        ResponseDTO responseDTO = userService.getAllUsers(token);
        return ResponseEntity.ok(responseDTO);
    }

    // 2. Get all users with their SIPs
    @GetMapping("/users/sips")
    public ResponseEntity<ResponseDTO> getAllUsersWithSips(@RequestHeader("Authorization") String token) {
        log.info("Admin fetching all users with their SIPs...");
        ResponseDTO responseDTO = userService.getAllUsersWithSips(token);
        return ResponseEntity.ok(responseDTO);
    }

}
