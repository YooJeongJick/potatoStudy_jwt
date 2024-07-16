package com.example.potatoStudy_jwt.error;

import com.example.potatoStudy_jwt.error.exception.NotFoundException;
import com.example.potatoStudy_jwt.error.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ErrorExceptionHandler {
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final NotFoundException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .errorCode(Integer.parseInt(e.getErrorCode().getCode()))
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }

    @ExceptionHandler({UnAuthorizedException.class})
    public ResponseEntity<ErrorEntity> exceptionHandler(final UnAuthorizedException e) {
        ErrorEntity errorEntity = ErrorEntity.builder()
                .errorCode(Integer.parseInt(e.getErrorCode().getCode()))
                .errorMessage(e.getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorEntity);
    }
}
