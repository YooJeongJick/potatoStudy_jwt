package com.example.potatoStudy_jwt.jwt;

import com.example.potatoStudy_jwt.RedisJwtService;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisJwtService redisJwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path. contains("/user/signUp") || path.contains("/user/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtProvider.resolveAccessToken(request);
        String refreshToken = jwtProvider.resolveRefreshToken(request);

        try {
            if (accessToken == null && refreshToken != null) {
                if (jwtProvider.validateToken(refreshToken) &&
                redisJwtService.isRefreshTokenValid(refreshToken) &&
                path.contains("/reissue")) {
                    filterChain.doFilter(request, response);
                } else {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        } catch (MalformedJwtException e) {}
    }

}
