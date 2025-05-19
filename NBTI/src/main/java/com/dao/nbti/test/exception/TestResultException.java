package com.dao.nbti.test.exception;

import com.dao.nbti.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class TestResultException extends RuntimeException {
    private final ErrorCode errorCode;

    public TestResultException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
