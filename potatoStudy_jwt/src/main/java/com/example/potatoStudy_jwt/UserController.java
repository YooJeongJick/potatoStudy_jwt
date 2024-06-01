package com.example.potatoStudy_jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public String signUp(@RequestBody UserDTO userDTO) {
        return userService.signUp(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping("/userGet")
    public Optional<User> userGet(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return userService.userGet(token);
    }

}
