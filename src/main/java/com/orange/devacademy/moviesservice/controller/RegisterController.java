package com.orange.devacademy.moviesservice.controller;

import com.orange.devacademy.moviesservice.model.User;
import com.orange.devacademy.moviesservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    public void register(@RequestBody User user) {
        boolean usernameAlreadyInUse = userService.isUsernameAlreadyInUse(user.getUsername());
        if(usernameAlreadyInUse) {
            throw new AuthenticationServiceException("Username already exists!");
        }
        userService.registerUser(user);
    }

}
