package com.example.potatoStudy_jwt;

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
    public ResponseEntity<UserDTO> userGet(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        UserDTO user = userService.userGet(token);
        return ResponseEntity.ok(user);
    }

}
