package com.tasksmaster.taskmaster.service;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tasksmaster.taskmaster.model.User;

@Service
public class TokenService {
    @Value("${JWT_SECRET}")
    private String secret;

    public String gerarToken(User user, String ip, String userAgent) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("auth-api")
                .withSubject(user.getEmail())
                .withClaim("ip", ip)
                .withClaim("userAgent", userAgent)
                .withExpiresAt(genExpirationDate())
                .sign(algorithm);
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public String getClaim(String token, String claimName) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getClaim(claimName)
                    .asString();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public Date genExpirationDate() {
        return Date.from(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")));
    }
}
