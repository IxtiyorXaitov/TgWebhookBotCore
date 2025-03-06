package com.ixtiyor.tgwebhookbotcore.dto.http;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult<T> {
    private String endpoint;
    private String message;

    private Integer httpStatus = 200;
    private String httpStatusText = "OK";

    private T content;


    public static <E> ApiResult<E> build(String message, E content) {
        return new ApiResult<>(
                message,
                content
        );
    }

    public static <E> ApiResult<E> build(E content) {
        return new ApiResult<>(
                content
        );
    }

    public static <E> ApiResult<E> build(String message) {
        return new ApiResult<>(
                message
        );
    }

    public ApiResult(String message) {
        this.message = message;
    }

    public ApiResult(T content) {
        this.content = content;
    }

    public ApiResult(String message, T content) {
        this.message = message;
        this.content = content;
    }

}
