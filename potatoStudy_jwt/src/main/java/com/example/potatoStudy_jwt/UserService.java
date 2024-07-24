package com.example.potatoStudy_jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.potatoStudy_jwt.error.ErrorCode;
import com.example.potatoStudy_jwt.error.exception.NotFoundException;
import com.example.potatoStudy_jwt.error.exception.UnAuthorizedException;
import com.example.potatoStudy_jwt.jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    // 회원 가입
    public void signUp(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UnAuthorizedException("이미 존재하는 이메일입니다.", ErrorCode.UNAUTHORIZED_EXCEPTION);
        }

        User user = userDTO.toEntity();
        userRepository.save(user);
    }

    // 로그인
    public HttpHeaders login(UserDTO userDTO) {
        String inputEmail = userDTO.getEmail();
        User user = userRepository.findByEmail(inputEmail);
        if (user == null)
            throw new NotFoundException("존재하지 않는 유저입니다.", ErrorCode.NOT_FOUND_EXCEPTION);

        String inputPassword = userDTO.getPassword();
        String password = user.getPassword();
        if (!inputPassword.equals(password))
            throw new UnAuthorizedException("비밀번호가 일치하지 않습니다.", ErrorCode.UNAUTHORIZED_EXCEPTION);

        // 토큰 생성
        String accessToken = jwtService.createAccessToken(user.getId());
        String refreshToken = jwtService.createRefreshToken(user.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("RefreshToken", "Bearer " + refreshToken);

        return headers;
    }

    // 유저 검색
    public UserDTO userGet(String token) {
        Long id = Long.valueOf(jwtService.verifyToken(token));
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            throw new NotFoundException("존재하지 않는 유저입니다.", ErrorCode.NOT_FOUND_EXCEPTION);

        return UserDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
