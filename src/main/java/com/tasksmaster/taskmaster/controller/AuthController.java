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
import com.tasksmaster.taskmaster.model.User;
import com.tasksmaster.taskmaster.service.TokenService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; // Do Spring Security

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto data) {
        // 1. Cria um token interno do Spring para validar as credenciais
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        
        // 2. O Spring Security tenta autenticar (compara email e senha criptografada)
        var auth = this.authenticationManager.authenticate(usernamePassword);
        
        // 3. Se deu certo, gera o Token JWT
        var token = tokenService.gerarToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}