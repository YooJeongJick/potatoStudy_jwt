package com.example.potatoStudy_jwt.error.exception;

import com.example.potatoStudy_jwt.error.ErrorCode;

public class UnAuthorizedException extends BusinessException{
    public UnAuthorizedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
