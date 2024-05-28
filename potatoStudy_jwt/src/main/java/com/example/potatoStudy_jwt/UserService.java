package com.example.potatoStudy_jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public String login(UserDTO userDTO) {
        String inputEmail = userDTO.getEmail();
        User user = userRepository.findByEmail(inputEmail);
        if (user == null) {
            return "잘못된 이메일";
        }

        String inputPassword = userDTO.getPassword();
        String password = user.getPassword();
        if (!inputPassword.equals(password)) {
            return "잘못된 비밀번호";
        }

        String token = jwtService.createToken(user.getId(), user.getEmail(), user.getPassword());
        return user.getEmail() + " 로그인 완료\n" + token;
    }

    public User userGet(String token) {
        DecodedJWT decodedJWT = jwtService.verifyToken(token);
        if (decodedJWT == null) {
            return null;
        }

        String email =  decodedJWT.getClaim("name").asString();
        return userRepository.findByEmail(email);
    }

}
