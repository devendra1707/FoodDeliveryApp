package com.food.delivery.controller;

import com.food.delivery.security.UserRequest;
import com.food.delivery.security.UserResponse;
import com.food.delivery.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping(value = "/_register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRequest userRequest){

        return userService.registerUser(userRequest);
    }

}
