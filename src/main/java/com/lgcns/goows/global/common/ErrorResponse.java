package com.lgcns.goows.global.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(HttpStatus status, String message) {
        this.statusCode = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(HttpStatus status, String message) {
        return new ErrorResponse(status, message);
    }
}