package com.dao.nbti.test.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/* 검사 결과를 반환 받는 DTO*/
@Getter
@Builder
public class AiAnswerResponse {

    private String aiAnswer;

}
