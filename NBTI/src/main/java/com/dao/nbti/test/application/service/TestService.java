package com.dao.nbti.test.application.service;

import com.dao.nbti.study.application.dto.response.ProblemResponseDto;
import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;

import java.util.List;


public interface TestService {

    List<ProblemResponseDto> getTestProblems(Integer userId);

    void createTestResult(TestResultCreateRequest testResultCreateRequest, Integer userId);

    void updateTestResult(Integer userId);

}