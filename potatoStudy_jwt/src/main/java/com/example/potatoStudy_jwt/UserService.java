package com.example.potatoStudy_jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    // 유저 등록
    public String signUp(UserDTO userDTO) {
        User user = userDTO.toEntity();
        userRepository.save(user);
        return user.getEmail() + " 회원가입 완료";
    }

    public ResponseEntity<String> login(UserDTO userDTO) {
        String inputEmail = userDTO.getEmail();
        User user = userRepository.findByEmail(inputEmail);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 이메일");
        }

        String inputPassword = userDTO.getPassword();
        String password = user.getPassword();
        if (!inputPassword.equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 비밀번호");
        }

        String accessToken = jwtService.createAccessToken(user.getId());
        String refreshToken = jwtService.createRefreshToken(user.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("RefreshToken", "Bearer " + refreshToken);

        return ResponseEntity.ok()
                .headers(headers)
                .body(user.getEmail() + " 로그인 완료");
    }

    public Optional<User> userGet(String token) {
        DecodedJWT decodedJWT = jwtService.verifyToken(token);
        if (decodedJWT == null) {
            return null;
        }

        Long id = decodedJWT.getClaim("id").asLong();
        return userRepository.findById(id);
    }

}
