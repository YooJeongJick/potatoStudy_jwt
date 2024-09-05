package com.example.potatoStudy_jwt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "유저 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 API")
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody UserDTO userDTO) {
        userService.signUp(userDTO);
        return ResponseEntity.ok().body("회원가입을 완료했습니다.");
    }

    @Operation(summary = "로그인 API")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        userService.login(userDTO, response);
        return ResponseEntity.ok().body("로그인을 완료했습니다.");
    }

    @Operation(summary = "유저 이메일 확인 API")
    @GetMapping("/userGet")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> userGet(HttpServletRequest request) {
        String userEmail = userService.userGet(request);
        return ResponseEntity.ok(userEmail);
    }

    @Operation(summary = "토큰 재발급 API")
    @GetMapping("/reissue")
    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        userService.reissueToken(request, response);
        return ResponseEntity.ok("토큰 재발급을 완료했습니다.");
    }

}
