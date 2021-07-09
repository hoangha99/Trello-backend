package com.blameo.trello.controller;

import com.blameo.trello.JwtUtility.JwtUtility;
import com.blameo.trello.config.UserDetailsServiceImpl;
import com.blameo.trello.model.User;
import com.blameo.trello.model.jwt.JwtRequest;
import com.blameo.trello.model.jwt.JwtRespone;
import com.blameo.trello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class HomeController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtility jwtUtility;


    @PostMapping("/register")
    public ResponseEntity<User> create(@RequestBody User user) {
        if (userRepository.checkExists(user.getEmail()) > 0 && !user.getEmail().equals(""))
            return new ResponseEntity("Email  is exist !", HttpStatus.INTERNAL_SERVER_ERROR);
        try {
            User us = new User();
            us.setEmail(user.getEmail());
            us.setPassword(passwordEncoder.encode(user.getPassword()));
            us.setRoleId(1L);
            userRepository.save(us);
            return new ResponseEntity<>(us, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/abc")
    public ResponseEntity<List<User>> getAll() {
        List<User> list = userRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
    }

    @PostMapping("/auth")
    public JwtRespone authenticate(@RequestBody JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getEmail(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            System.out.println(e);
        }

        final UserDetails userDetails
                = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        final String token = jwtUtility.generateToken(userDetails);
        return new JwtRespone(token + " " + userDetails.getAuthorities());
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody JwtRequest jwtRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());

            String jwt = jwtUtility.generateToken(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(new JwtRespone(jwt));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/hello")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String hello() {
        return "Hello";
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/gudbye")
    public String bye() {
        return "bye";
    }

}
