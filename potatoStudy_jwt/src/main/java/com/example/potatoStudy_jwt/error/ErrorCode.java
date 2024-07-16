package com.example.potatoStudy_jwt.error;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "401", "401 UNAUTHORIZED"),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "404", "404 NOT_FOUND");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
