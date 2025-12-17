package org.sopt.domain.common;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                true,
                "요청이 성공적으로 처리되었습니다.",
                data,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                LocalDateTime.now()
        );
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(
                false,
                message,
                null,
                LocalDateTime.now()
        );
    }
}