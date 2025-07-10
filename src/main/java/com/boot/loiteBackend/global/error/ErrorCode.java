package com.boot.loiteBackend.global.error;

import lombok.Cleanup;
import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String getCode();

    String getMessage();

    HttpStatus getStatus();

}