package com.example.demo_app.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo_app.model.User;
import com.example.demo_app.repository.UserRepository;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public User registerUser(String username, String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String username, String password){
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }else{
            return Optional.empty();
        }
    }

    public Optional<User> findByUsername(String Username){
        return userRepository.findByUsername(Username);
    }

    public List<User> fetchdb(){
        return userRepository.findAll();
    }

    public String deleteUser(String username){
        Optional<User> deletedUser = userRepository.findByUsername(username);
        if(deletedUser.isEmpty()){
            return "No such user";
        }
        userRepository.delete(deletedUser.get());
        return "User deleted from DB";
        
    }

    
}
