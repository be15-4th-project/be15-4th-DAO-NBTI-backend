package com.dao.nbti.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {
    //회원 오류 (00000 ~ 09999)
    LOGIN_ID_ALREADY_EXISTS("10001", "이미 사용중인 ID 입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("10002","존재하지 않는 사용자입니다.",HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("10003", "올바르지 않은 아이디 혹은 비밀번호입니다.", HttpStatus.UNAUTHORIZED),

    // 학습 관련 오류 (20001 ~ 29999)
    CATEGORY_NOT_FOUND("20001", "존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND),
    ANSWER_TYPE_NOT_FOUND("20002", "존재하지 않는 답안 유형입니다.", HttpStatus.NOT_FOUND),
    PROBLEM_NOT_FOUND("20003", "해당하는 조건의 문제가 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // 공통 오류
    VALIDATION_ERROR("90001", "입력 값 검증 오류입니다.", HttpStatus.BAD_REQUEST),
    UNKNOWN_RUNTIME_ERROR("90002", "알 수 없는 런타임 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_ERROR("90003", "알 수 없는 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
