package com.dao.nbti.test.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;
import com.dao.nbti.test.domain.aggregate.IsSaved;
import com.dao.nbti.test.domain.aggregate.TestResult;
import com.dao.nbti.test.domain.repository.TestProblemRepository;
import com.dao.nbti.test.domain.repository.TestResultRepository;
import com.dao.nbti.test.exception.TestException;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import com.dao.nbti.user.exception.UserException;
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
    private final UserRepository userRepository;

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

    /* 마이페이지에 검사 결과 저장하기*/
    @Transactional
    public void updateTestResult(Integer userId) {

        // userId 가져오기 (로그인 된 user의 Id)
        // user가 있는지 확인하기
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        log.info("userId: {}", user.getUserId());

        // 검사 결과 가져오기
        TestResult testResult = testResultRepository.findLatestByUserId(userId)
                .orElseThrow(() -> new TestException(ErrorCode.TEST_RESULT_NOT_FOUND));

        // 검사 결과 수정하기
        testResult.saveToMyPage();
    }
}