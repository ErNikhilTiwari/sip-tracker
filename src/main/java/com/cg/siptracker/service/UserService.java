package com.cg.siptracker.service;

import com.cg.siptracker.model.User;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByEmail(String email);
}