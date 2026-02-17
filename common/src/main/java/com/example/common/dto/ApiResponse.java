package com.example.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "공통 API 응답 래퍼")
public class ApiResponse<T> {

    @Schema(description = "HTTP 상태 코드", example = "200")
    private int status;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message, null);
    }
}
