package com.dao.nbti.test.application.service;

import com.dao.nbti.test.application.dto.request.AiAnswerCreateRequest;
import com.dao.nbti.test.application.dto.response.AiAnswerResponse;

public interface TestAiAnswerService {

    AiAnswerResponse createAiAnswer(AiAnswerCreateRequest request);

}