package com.example.potatoStudy_jwt.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.potatoStudy_jwt.User;
import com.example.potatoStudy_jwt.UserDTO;
import com.example.potatoStudy_jwt.UserRepository;
import com.example.potatoStudy_jwt.error.ErrorCode;
import com.example.potatoStudy_jwt.error.exception.NotFoundException;
import com.example.potatoStudy_jwt.error.exception.UnAuthorizedException;
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

    public void signUp(UserDTO userDTO) {
        User user = userDTO.toEntity();
        userRepository.save(user);
    }

    public HttpHeaders login(UserDTO userDTO) {
        String inputEmail = userDTO.getEmail();
        User user = userRepository.findByEmail(inputEmail);
        if (user == null)
            throw new NotFoundException("존재하지 않는 유저", ErrorCode.NOT_FOUND_EXCEPTION);

        String inputPassword = userDTO.getPassword();
        String password = user.getPassword();
        if (!inputPassword.equals(password))
            throw new UnAuthorizedException("비밀번호 불일치", ErrorCode.UNAUTHORIZED_EXCEPTION);

        String accessToken = jwtService.createAccessToken(user.getId());
        String refreshToken = jwtService.createRefreshToken(user.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("RefreshToken", "Bearer " + refreshToken);

        return headers;
    }

    public UserDTO userGet(String token) {
        DecodedJWT decodedJWT = jwtService.verifyToken(token);

        Long id = decodedJWT.getClaim("id").asLong();
        User user = userRepository.findById(id).orElse(null);

        return UserDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
