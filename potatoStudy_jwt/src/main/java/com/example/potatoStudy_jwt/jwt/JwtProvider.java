package com.example.potatoStudy_jwt.jwt;

import com.example.potatoStudy_jwt.RedisJwtService;
import com.example.potatoStudy_jwt.User;
import com.example.potatoStudy_jwt.UserRepository;
import com.example.potatoStudy_jwt.error.ErrorCode;
import com.example.potatoStudy_jwt.error.exception.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserRepository userRepository;
    private final RedisJwtService redisJwtService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.accessTokenExp}")
    private Long accessTokenExpMinutes;

    @Value("${jwt.refreshTokenExp}")
    private Long refreshTokenExpMinutes;

    public String createAccessToken(String email){
        return this.createToken(email, accessTokenExpMinutes);
    }

    public String createRefreshToken(String email) {
        return this.createToken(email, refreshTokenExpMinutes);
    }

    public String createToken(String email, Long validTime) {
        Claims claims = Jwts.claims().setSubject(email);
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

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Authorization", "Bearer " + accessToken);
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("RefreshToken", "Bearer " + refreshToken);
    }

    public String resolveAccessToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") != null)
            return request.getHeader("Authorization").substring(7);
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getHeader("RefreshToken") != null)
            return request.getHeader("RefreshToken").substring(7);
        return null;
    }

    public String reissueAccessToken(String refreshToken) {
        String email = redisJwtService.getValues(refreshToken).get("email");
        if (Objects.isNull(email))
            throw new NotFoundException("존재하지 않는 유저입니다.", ErrorCode.NOT_FOUND_EXCEPTION);

        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new NotFoundException("존재하지 않는 유저입니다.", ErrorCode.NOT_FOUND_EXCEPTION);

        return createAccessToken(email);
    }

    public String reissueRefreshToken(String refreshToken) {
        String email = redisJwtService.getValues(refreshToken).get("email");
        if (Objects.isNull(email))
            throw new NotFoundException("존재하지 않는 유저입니다.", ErrorCode.NOT_FOUND_EXCEPTION);

        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new NotFoundException("존재하지 않는 유저입니다.", ErrorCode.NOT_FOUND_EXCEPTION);

        String newRefreshToken = createRefreshToken(email);

        redisJwtService.delValues(refreshToken);
        redisJwtService.setValues(newRefreshToken, email);

        return newRefreshToken;
    }

}
