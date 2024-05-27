package com.example.potatoStudy_jwt;

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

        // 로그인 성공 시 JWT 토큰 생성 및 반환
        String token = jwtService.createToken(user.getId(), user.getEmail(), user.getPassword());
        return user.getEmail() + " 로그인 완료\n" + token;
    }
}
