package com.tasksmaster.taskmaster.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tasksmaster.taskmaster.model.User;
import com.tasksmaster.taskmaster.repository.UserRepository;
import com.tasksmaster.taskmaster.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        var token = this.recoverToken(request);
        
        if (token != null) {
            var login = tokenService.validarToken(token);
            
            // Pegamos os dados da requisição ATUAL
            String currentIp = request.getRemoteAddr();
            String currentUserAgent = request.getHeader("User-Agent");
            
            // Pegamos os dados de dentro do TOKEN
            String tokenIp = tokenService.getClaim(token, "ip");
            String tokenUA = tokenService.getClaim(token, "userAgent");

            boolean isIpValid = false;

            if (tokenIp != null && currentIp != null) {
                isIpValid = currentIp.equals(tokenIp) || 
                            (currentIp.equals("0:0:0:0:0:0:0:1") && "127.0.0.1".equals(tokenIp)) ||
                            ("127.0.0.1".equals(currentIp) && "0:0:0:0:0:0:0:1".equals(tokenIp));
            }

            boolean isUaValid = (tokenUA == null) || tokenUA.equals(currentUserAgent);

            if (login != null && (isIpValid && isUaValid) && currentUserAgent.equals(tokenUA)) {
                
                var email = tokenService.validarToken(token); 
                if (email != null) {
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } else {
                
                throw new RuntimeException("Token utilizado em dispositivo/rede diferente!");
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}