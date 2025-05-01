//package com.cg.siptracker.controller;
//
//import com.cg.siptracker.dto.UserDTO;
//import com.cg.siptracker.dto.ResponseDTO;
//import com.cg.siptracker.service.UserService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<ResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
//        ResponseDTO newUser = userService.registerUser(userDTO);
//        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ResponseDTO> getUserById(@PathVariable Long id) {
//        ResponseDTO user = userService.getUserById(id);
//        return ResponseEntity.ok(user);
//    }
//
//    @GetMapping("/username/{username}")
//    public ResponseEntity<ResponseDTO> getUserByUsername(@PathVariable String username) {
//        ResponseDTO user = userService.getUserByUsername(username);
//        return ResponseEntity.ok(user);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ResponseDTO>> getAllUsers() {
//        List<ResponseDTO> users = userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ResponseDTO> updateUser(
//            @PathVariable Long id,
//            @Valid @RequestBody UserDTO userDTO
//    ) {
//        ResponseDTO User = userService.User(id, userDTO);
//        return ResponseEntity.ok(User);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
//}