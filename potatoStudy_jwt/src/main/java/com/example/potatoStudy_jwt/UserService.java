package com.example.potatoStudy_jwt;

import com.example.potatoStudy_jwt.error.ErrorCode;
import com.example.potatoStudy_jwt.error.exception.NotFoundException;
import com.example.potatoStudy_jwt.error.exception.UnAuthorizedException;
import com.example.potatoStudy_jwt.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RedisJwtService redisJwtService;

    public void signUp(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UnAuthorizedException("이미 존재하는 이메일입니다.", ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        User user = userDTO.toEntity();
        userRepository.save(user);
    }

    public void login(UserDTO userDTO, HttpServletResponse response) {
        String inputEmail = userDTO.getEmail();
        User user = userRepository.findByEmail(inputEmail);
        if (user == null)
            throw new NotFoundException("존재하지 않는 유저입니다.", ErrorCode.NOT_FOUND_EXCEPTION);

        String inputPassword = userDTO.getPassword();
        String password = user.getPassword();
        if (!inputPassword.equals(password))
            throw new UnAuthorizedException("비밀번호가 일치하지 않습니다.", ErrorCode.UNAUTHORIZED_EXCEPTION);

        this.setJwtTokenInHeader(user.getEmail(), response);
    }

    public UserDTO userGet(String token) {
        String email = String.valueOf(jwtProvider.verifyToken(token));
        User user = userRepository.findByEmail(email);
        if (user == null)
            throw new NotFoundException("존재하지 않는 유저입니다.", ErrorCode.NOT_FOUND_EXCEPTION);

        return UserDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public void setJwtTokenInHeader(String email, HttpServletResponse response) {
        String accessToken = jwtProvider.createAccessToken(email);
        String refreshToken = jwtProvider.createRefreshToken(email);

        jwtProvider.setHeaderAccessToken(response, accessToken);
        jwtProvider.setHeaderRefreshToken(response, refreshToken);

        redisJwtService.setValues(refreshToken, email);
    }

}
