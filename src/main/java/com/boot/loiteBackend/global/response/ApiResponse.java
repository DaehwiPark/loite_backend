package com.boot.loiteBackend.global.response;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String errorCode;
    private T data;
    private Map<String, Object> extra;

}
