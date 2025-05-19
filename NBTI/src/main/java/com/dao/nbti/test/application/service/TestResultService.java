package com.dao.nbti.test.application.service;

import com.dao.nbti.test.application.dto.response.TestResultDetailResponse;
import com.dao.nbti.test.application.dto.request.TestResultSearchCondition;
import com.dao.nbti.test.application.dto.response.TestResultSummaryResponse;
import com.dao.nbti.test.domain.aggregate.TestResult;
import com.dao.nbti.test.domain.repository.TestResultRepository;
import com.dao.nbti.test.exception.TestResultException;
import com.dao.nbti.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestResultService {

    private final TestResultRepository testResultRepository;

    /**
     * 검사 결과 목록 조회
     */
    public Page<TestResultSummaryResponse> getTestResultList(TestResultSearchCondition condition, Pageable pageable) {
        Page<TestResult> results = testResultRepository.findByUserAndCondition(
                condition.getUserId(),
                condition.getYear(),
                condition.getMonth(),
                pageable
        );

        return results.map(this::toSummaryResponse);
    }

    /**
     * 검사 결과 상세 조회
     */
    public TestResultDetailResponse getTestResultDetail(int testResultId, int userId) {
        TestResult result = testResultRepository.findByTestResultIdAndUserId(testResultId, userId)
                .orElseThrow(() -> new TestResultException(ErrorCode.TEST_RESULT_NOT_FOUND));

        // 권한 체크가 별도 필요하다면 아래 주석 해제
        // if (result.getUserId() != userId) {
        //     throw new TestResultException(ErrorCode.UNAUTHORIZED_TEST_RESULT_ACCESS);
        // }

        return toDetailResponse(result);
    }

    /**
     * 목록 응답 DTO 변환
     */
    private TestResultSummaryResponse toSummaryResponse(TestResult result) {
        int[] scores = {
                result.getLangComp(),
                result.getGeneralKnowledge(),
                result.getPercReason(),
                result.getWorkMemory(),
                result.getProcSpeed(),
                result.getSpatialPerception()
        };
        String[] categories = {
                "언어 이해", "시사 상식", "지각 추론", "작업 기억", "처리 속도", "공간 지각력"
        };

        int maxIdx = 0, minIdx = 0;
        for (int i = 1; i < scores.length; i++) {
            if (scores[i] > scores[maxIdx]) maxIdx = i;
            if (scores[i] < scores[minIdx]) minIdx = i;
        }

        int totalScore = (int) Math.round(
                (scores[0] + scores[1] + scores[2] + scores[3] + scores[4] + scores[5]) / 6.0
        );

        return TestResultSummaryResponse.builder()
                .testResultId(result.getTestResultId())
                .createdAt(result.getCreatedAt())
                .highestCategory(categories[maxIdx])
                .lowestCategory(categories[minIdx])
                .totalScore(totalScore)
                .build();
    }

    /**
     * 상세 응답 DTO 변환
     */
    private TestResultDetailResponse toDetailResponse(TestResult result) {
        return TestResultDetailResponse.builder()
                .langComp(result.getLangComp())
                .generalKnowledge(result.getGeneralKnowledge())
                .percReason(result.getPercReason())
                .workMemory(result.getWorkMemory())
                .procSpeed(result.getProcSpeed())
                .spatialPerception(result.getSpatialPerception())
                .aiText(result.getAiText())
                .createdAt(result.getCreatedAt())
                .build();
    }
}
