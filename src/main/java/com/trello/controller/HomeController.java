package com.trello.controller;

import com.trello.JwtUtility.JwtUtility;
import com.trello.Util.FileUpload;
import com.trello.Util.Validation;
import com.trello.config.UserDetailsServiceImpl;
import com.trello.model.AuthenticationProvider;
import com.trello.model.User;
import com.trello.model.dto.SearchUserDto;
import com.trello.model.dto.UserDto;
import com.trello.model.jwt.JwtRequest;
import com.trello.model.jwt.JwtRespone;
import com.trello.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
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

    @Autowired
    FileUpload fileUpload;


    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody UserDto user) {
        if (!Validation.validateEmail(user.getEmail()))
            return new ResponseEntity("Email is invalid", HttpStatus.INTERNAL_SERVER_ERROR);
        if (userRepository.getUserByEmail(user.getEmail()).isPresent())
            return new ResponseEntity("Email is exist", HttpStatus.INTERNAL_SERVER_ERROR);
        if (!Validation.validatePassword(user.getPassword()))
            return new ResponseEntity("Password longer than six characters ", HttpStatus.INTERNAL_SERVER_ERROR);
        if (!user.getPassword().equals(user.getRePassword()))
            return new ResponseEntity("Re-enter password does not match", HttpStatus.INTERNAL_SERVER_ERROR);
        if (user.getFullName().equals(""))
            return new ResponseEntity("Full name is null", HttpStatus.INTERNAL_SERVER_ERROR);
        try {
            User us = new User();
            us.setEmail(user.getEmail());
            us.setFullName(user.getFullName());
            us.setPassword(passwordEncoder.encode(user.getPassword()));
            us.setRoleId(1L);
            us.setAuthProvider(AuthenticationProvider.LOCAL);
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
        if (jwtRequest.getPassword().equals(""))
            return new ResponseEntity("Password is invalid", HttpStatus.INTERNAL_SERVER_ERROR);
        if (jwtRequest.getEmail().equals(""))
            return new ResponseEntity("Email is invalid", HttpStatus.INTERNAL_SERVER_ERROR);
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
        User user = userRepository.findByUsername(authentication.getName());
        SearchUserDto searchUserDto = new SearchUserDto();
        BeanUtils.copyProperties(user, searchUserDto);
        return new ResponseEntity<>(searchUserDto, HttpStatus.OK);
    }


    @PostMapping("/upload-file")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        System.out.println(file.getContentType());
        if(file.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("wrong");
        }
        if(!file.getContentType().equals("image/jpeg")){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("wrong");
        }
        System.out.println(fileUpload.uploadFile(file));
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource> downLoadSingleFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileUpload.downloadFile(fileName);
        String mimeType;

        try {
            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        mimeType = mimeType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mimeType;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }
    @GetMapping("/hello")
    public String restricted() {
        return "if you see this you are logged in";
    }
}
