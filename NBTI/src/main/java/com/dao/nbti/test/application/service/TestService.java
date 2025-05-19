package com.dao.nbti.test.application.service;

import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;

public interface TestService {

    void createTestResult(TestResultCreateRequest testResultCreateRequest, Integer userId);

}
