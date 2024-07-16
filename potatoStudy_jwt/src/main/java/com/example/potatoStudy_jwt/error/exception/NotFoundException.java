package com.example.potatoStudy_jwt.error.exception;


import com.example.potatoStudy_jwt.error.ErrorCode;

public class NotFoundException extends BusinessException{
    public NotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
