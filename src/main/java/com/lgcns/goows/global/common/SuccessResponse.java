package com.lgcns.goows.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
    private String status;
    private String message;
    private T data;

    public static <T> SuccessResponse<T> success(String message, T data) {
        return new SuccessResponse<>("SUCCESS", message, data);
    }

    public static <T> SuccessResponse<T> success(T data) {
        return new SuccessResponse<>("SUCCESS", "요청이 성공했습니다.", data);
    }

    public static <T> SuccessResponse<T> success() {
        return new SuccessResponse<>("SUCCESS", "요청이 성공했습니다.", null);
    }
}