package com.dao.nbti.test.application.service;

import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;
import com.dao.nbti.test.domain.aggregate.TestResult;
import com.dao.nbti.test.domain.repository.TestProblemRepository;
import com.dao.nbti.test.domain.repository.TestResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {

    private final TestResultRepository testResultRepository;
    private final TestProblemRepository testProblemRepository;
    private final TestAiAnswerService testAiAnswerService;

    /* 검사 결과 생성하기*/
    @Transactional
    public void createTestResult(TestResultCreateRequest request, Integer userId) {

        /* ai 분석 결과*/
        String aiText = testAiAnswerService.createAiAnswer(request);

        /* 검사 결과 생성하기 */
        TestResult testResult = TestResult.builder()
                .userId(userId)
                .langComp(request.getLangComp())
                .percReason(request.getPercReason())
                .generalKnowledge(request.getGeneralKnowledge())
                .procSpeed(request.getProcSpeed())
                .spatialPerception(request.getSpatialPerception())
                .workMemory(request.getWorkMemory())
                .createdAt(LocalDateTime.now())
                .aiText(aiText)
                .build();

        /* 검사 결과 저장하기*/
        testResultRepository.save(testResult);
    }

}