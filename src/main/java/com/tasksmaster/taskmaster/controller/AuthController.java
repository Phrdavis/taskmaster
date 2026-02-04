package com.tasksmaster.taskmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tasksmaster.taskmaster.dto.LoginDto;
import com.tasksmaster.taskmaster.dto.LoginResponseDto;
import com.tasksmaster.taskmaster.dto.UserDto;
import com.tasksmaster.taskmaster.model.User;
import com.tasksmaster.taskmaster.service.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; // Do Spring Security

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto data, HttpServletRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        
        var token = tokenService.gerarToken((User) auth.getPrincipal(), ipAddress, userAgent);

        return ResponseEntity.ok(new LoginResponseDto(token, new UserDto((User) auth.getPrincipal())));
    }
}