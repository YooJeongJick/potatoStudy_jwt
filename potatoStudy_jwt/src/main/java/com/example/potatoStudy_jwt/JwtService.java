package com.example.potatoStudy_jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    static Long EXPIRE_TIME = 60L * 60L * 1000L;
    @Value("${jwt.secret}")
    private String secretKey;

    public String createToken(Long id, String email, String password) {
        Date tokenExp = new Date(System.currentTimeMillis() + (EXPIRE_TIME));
        Date tokeniat = new Date(System.currentTimeMillis());

        String createToken = JWT.create()
                .withExpiresAt(tokenExp)
                .withIssuedAt(tokeniat)
                .withClaim("name", email)
                .sign(Algorithm.HMAC512(secretKey));

        return createToken;
    }
}
