package com.example.potatoStudy_jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String password;

    public User toEntity() {
        User user = User.builder()
                .email(this.email)
                .password(this.password)
                .build();
        return user;
    }
}
