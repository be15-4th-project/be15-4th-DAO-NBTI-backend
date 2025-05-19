package com.dao.nbti.test.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.domain.aggregate.Category;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.test.application.dto.request.TestResultSearchCondition;
import com.dao.nbti.test.application.dto.response.TestResultDetailResponse;
import com.dao.nbti.test.application.dto.response.CategoryScoreDetail;
import com.dao.nbti.test.application.dto.response.TestResultSummaryResponse;
import com.dao.nbti.test.domain.aggregate.TestResult;
import com.dao.nbti.test.domain.repository.TestResultRepository;
import com.dao.nbti.test.exception.TestResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestResultService {

    private final TestResultRepository testResultRepository;
    private final CategoryRepository categoryRepository;

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
        List<String> categoryNames = List.of(
                "언어 이해", "시사 상식", "지각 추론", "작업 기억", "처리 속도", "공간 지각력"
        );

        // 상위 분야만 필터링해서 설명 맵 생성
        Map<String, String> descriptionMap = categoryRepository.findAll().stream()
                .filter(c -> c.getParentCategoryId() == null && categoryNames.contains(c.getName()))
                .collect(Collectors.toMap(Category::getName, Category::getDescription));

        List<CategoryScoreDetail> scores = List.of(
                newScoreDetail("언어 이해", result.getLangComp(), descriptionMap),
                newScoreDetail("시사 상식", result.getGeneralKnowledge(), descriptionMap),
                newScoreDetail("지각 추론", result.getPercReason(), descriptionMap),
                newScoreDetail("작업 기억", result.getWorkMemory(), descriptionMap),
                newScoreDetail("처리 속도", result.getProcSpeed(), descriptionMap),
                newScoreDetail("공간 지각력", result.getSpatialPerception(), descriptionMap)
        );

        return TestResultDetailResponse.builder()
                .scores(scores)
                .aiText(result.getAiText())
                .createdAt(result.getCreatedAt())
                .build();
    }

    private CategoryScoreDetail newScoreDetail(String name, int score, Map<String, String> descMap) {
        return CategoryScoreDetail.builder()
                .categoryName(name)
                .description(descMap.getOrDefault(name, ""))
                .score(score)
                .build();
    }
}
