package com.dao.nbti.test.application.service;

import com.dao.nbti.test.application.dto.request.AdminTestResultSearchCondition;
import com.dao.nbti.test.application.dto.request.TestResultSearchCondition;
import com.dao.nbti.test.application.dto.response.AdminTestResultSummaryResponse;
import com.dao.nbti.test.application.dto.response.TestResultDetailResponse;
import com.dao.nbti.test.application.dto.response.TestResultSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestResultService {
    Page<TestResultSummaryResponse> getTestResultList(TestResultSearchCondition condition, Pageable pageable);
    TestResultDetailResponse getTestResultDetail(int testResultId, int userId);
    AdminTestResultSummaryResponse getAdminTestResultList(AdminTestResultSearchCondition condition, Pageable pageable);
}
