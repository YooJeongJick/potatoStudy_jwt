package com.example.potatoStudy_jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    static Long accessTokenExpMinutes = 60L * 60L * 1000L;
    static Long refreshTokenExpMinutes = 2L * 7L * 24L * 60L * 60L * 1000L;
    @Value("${jwt.secret}")
    private String secretKey;

    public String createAccessToken(Long id){
        return this.createToken(id, accessTokenExpMinutes);
    }

    public String createRefreshToken(Long id) {
        return this.createToken(id, refreshTokenExpMinutes);
    }

    public String createToken(Long id, Long validTime) {
        String createToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + validTime))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withClaim("id", id)
                .sign(Algorithm.HMAC512(secretKey));

        return createToken;
    }

    public DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

}
