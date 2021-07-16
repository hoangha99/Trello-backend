package com.blameo.trello.controller;

import com.blameo.trello.JwtUtility.JwtUtility;
import com.blameo.trello.Util.Validation;
import com.blameo.trello.config.UserDetailsServiceImpl;
import com.blameo.trello.model.User;
import com.blameo.trello.model.dto.UserDto;
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
    public ResponseEntity<?> create(@RequestBody UserDto user) {
        if (!Validation.validateEmail(user.getEmail()))
            return new ResponseEntity("Email is invalid", HttpStatus.INTERNAL_SERVER_ERROR);
        if (userRepository.checkExists(user.getEmail()) > 0)
            return new ResponseEntity("Email is exist", HttpStatus.INTERNAL_SERVER_ERROR);
        if (!Validation.validatePassword(user.getPassword()))
            return new ResponseEntity("Password longer than six characters ", HttpStatus.INTERNAL_SERVER_ERROR);
        if (!user.getPassword().equals(user.getRePassword()))
            return new ResponseEntity("Re-enter password does not match", HttpStatus.INTERNAL_SERVER_ERROR);
        try {
            User us = new User();
            us.setEmail(user.getEmail());
            us.setPassword(passwordEncoder.encode(user.getPassword()));
            us.setRoleId(1L);
            userRepository.save(us);
            return new ResponseEntity(us, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll() {
        List<User> list = userRepository.findAll();
        return new ResponseEntity<>(list, HttpStatus.ACCEPTED);
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
            return new ResponseEntity<>("Email/Password invalid", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getInfo")
    public ResponseEntity<?> getName(Authentication authentication) {
        String name = authentication.getName();
        return new ResponseEntity<>(userRepository.findByUsername(name), HttpStatus.OK);
    }

}
