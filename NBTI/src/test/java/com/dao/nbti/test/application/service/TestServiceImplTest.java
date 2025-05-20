package com.dao.nbti.test.application.service;

import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;
import com.dao.nbti.test.domain.repository.TestProblemRepository;
import com.dao.nbti.test.domain.repository.TestResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @Mock
    private TestResultRepository testResultRepository;

    @Mock
    private TestProblemRepository testProblemRepository;

    @Mock
    private TestAiAnswerService testAiAnswerService;

    @InjectMocks
    private TestServiceImpl testService;

    @Test
    void createTestResult() {
        Integer userId = 1;
        TestResultCreateRequest request = new TestResultCreateRequest
                (4, 5, 3, 6, 2, 1);

        String aiText = "당신은 논리적인 사고가 뛰어납니다.";
        Mockito.when(testAiAnswerService.createAiAnswer(request)).thenReturn(aiText);

        testService.createTestResult(request, userId);

        // times(1) 을 통해 메서드가 1번 실행되엇는지 점검
        Mockito.verify(testResultRepository, Mockito.times(1)).save(Mockito.argThat(testResult ->
                testResult.getUserId().equals(userId)
                        && testResult.getAiText().equals(aiText)
                        && testResult.getLangComp() == 4
                        && testResult.getGeneralKnowledge() == 5
        ));
    }

    @Test
    void saveTestResult() {
        Integer userId = 1;
        TestResultCreateRequest request = new TestResultCreateRequest(4, 5, 3, 6, 2, 1); // 생성자 또는 빌더로 채워줘야 함

        String aiText = "당신은 논리적인 사고가 뛰어납니다.";
        Mockito.when(testAiAnswerService.createAiAnswer(request)).thenReturn(aiText);

        testService.createTestResult(request, userId);

        Mockito.verify(testResultRepository, Mockito.times(1)).save(Mockito.argThat(testResult ->
                testResult.getUserId().equals(userId)
                        && testResult.getAiText().equals(aiText)
                        && testResult.getLangComp() == 4
                        && testResult.getGeneralKnowledge() == 5
        ));
    }


}
