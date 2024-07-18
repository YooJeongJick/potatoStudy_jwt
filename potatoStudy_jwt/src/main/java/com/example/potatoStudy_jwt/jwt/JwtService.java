package com.example.potatoStudy_jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.accessTokenExp}")
    private Long accessTokenExpMinutes;

    @Value("${jwt.refreshTokenExp}")
    private Long refreshTokenExpMinutes;

    public String createAccessToken(Long id){
        return this.createToken(id, accessTokenExpMinutes);
    }

    public String createRefreshToken(Long id) {
        return this.createToken(id, refreshTokenExpMinutes);
    }

    public String createToken(Long id, Long validTime) {
        Claims claims = Jwts.claims().setSubject(id.toString());
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + validTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String verifyToken(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();

        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }

}
