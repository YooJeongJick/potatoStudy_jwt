package com.example.potatoStudy_jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {
        userService.signUp(userDTO);
        return ResponseEntity.ok().body("회원가입을 완료했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        userService.login(userDTO, response);
        return ResponseEntity.ok().body("로그인을 완료했습니다.");
    }

    @GetMapping("/userGet")
    public ResponseEntity<String> userGet(HttpServletRequest request) {
        String userEmail = userService.userGet(request);
        return ResponseEntity.ok(userEmail);
    }

    @GetMapping("/reissue")
    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok("토큰 재발급을 완료했습니다.");
    }

}
