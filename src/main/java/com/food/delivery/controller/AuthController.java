package com.food.delivery.controller;

import com.food.delivery.security.AuthenticationRequest;
import com.food.delivery.security.AuthenticationResponse;
import com.food.delivery.service.AppUserDetailsService;
import com.food.delivery.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,AppUserDetailsService userDetailsService,JwtUtil jwtUtil){
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/_login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(request.getEmail(),jwtToken);
    }

}
