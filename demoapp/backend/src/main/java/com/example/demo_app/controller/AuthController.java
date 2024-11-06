package com.example.demo_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo_app.model.User;
import com.example.demo_app.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;


@RestController
@RequestMapping("/api")
public class AuthController {
    
    @Autowired
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> fetchdb(){
        List<User> list = userService.fetchdb();
        if(list.isEmpty()){
            return ResponseEntity.ok("Database empty");
        }
        return ResponseEntity.ok(list);

    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String status = userService.deleteUser(username);
        return ResponseEntity.ok(status);
    }
    
    

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        if(userService.findByUsername(user.getUsername()).isPresent()){

            return ResponseEntity.badRequest().body("Username is aldready taken");

        }
        userService.registerUser(user.getUsername(), user.getPassword());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        Optional<User> existingUser = userService.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            Optional<User> logUser = userService.loginUser(user.getUsername(),user.getPassword());
            
            if (logUser.isPresent()) {
            return ResponseEntity.ok("Login successful");
            }
        }
        
        return ResponseEntity.status(401).body("Invalid Credentials");
    }
    
}
