package com.lgcns.goows.global.exception;

import com.lgcns.goows.global.common.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("[IllegalArgumentException] {}", e.getMessage());
        ErrorResponse error = ErrorResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
        // Content-Type을 명시적으로 application/json으로 설정
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }

    // MethodArgumentNotValidException 처리 (DTO @Valid 실패)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError() != null ?
                e.getBindingResult().getFieldError().getDefaultMessage() : "유효성 검사 실패";
        log.error("[ValidationException] {}", message);
        ErrorResponse error = ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }

    // ConstraintViolationException 처리 (ex. @RequestParam, @PathVariable의 유효성 검사)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("[ConstraintViolationException] {}", e.getMessage());
        ErrorResponse error = ErrorResponse.of(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }
    // 로그인 정보 불일치
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error("[BadCredentialsException] {}", e.getMessage());
        ErrorResponse error = ErrorResponse.of(HttpStatus.BAD_REQUEST, "회원 정보가 올바르지 않습니다");
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }

    // 그 외의 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        log.error("[UnhandledException] ", e);
        ErrorResponse error = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }
}