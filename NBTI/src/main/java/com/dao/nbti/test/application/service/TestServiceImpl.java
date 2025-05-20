package com.dao.nbti.test.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.domain.aggregate.AnswerType;
import com.dao.nbti.problem.domain.aggregate.Category;
import com.dao.nbti.problem.domain.aggregate.Problem;
import com.dao.nbti.problem.domain.repository.AnswerTypeRepository;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.problem.domain.repository.ProblemTestRepository;
import com.dao.nbti.study.application.dto.response.ProblemResponseDto;
import com.dao.nbti.study.exception.NoSuchAnswerTypeException;
import com.dao.nbti.study.exception.NoSuchCategoryException;
import com.dao.nbti.study.exception.ProblemNotFoundException;
import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;
import com.dao.nbti.test.domain.aggregate.TestResult;
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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl implements TestService {

    private final TestResultRepository testResultRepository;
    private final com.dao.nbti.test.domain.repository.TestProblemRepository testProblemRepository;
    private final ProblemTestRepository problemTestRepository;
    private final TestAiAnswerService testAiAnswerService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerTypeRepository answerTypeRepository;

    /* 검사 문제 제공하기 */
    @Transactional
    public List<ProblemResponseDto>  getTestProblems(Integer userId) {
        // 1. 문제를 담아서 반환하는 list 선언
        List<ProblemResponseDto> problemList = new ArrayList<>();

        // 2. 비회원인 경우에는 맛보기 검사 제공 (분야별 1개씩 랜덤으로 문제 제공)
        if (userId == null) {
            for(int i=1; i<=6; i++) {
                Problem problem = problemTestRepository.findSampleProblemByParentCategory(i)
                        .orElseThrow(() -> new ProblemNotFoundException(ErrorCode.PROBLEM_NOT_FOUND));

                problemList.add(problemResponseDto(problem));
            }
        } else {
            // userId가 있는 경우에는 18문제를 제공 (분야별 + 레벨별로 문제 제공)
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

            /* 포인트가 5보다 적은 경우에 예외 발생*/
            if(user.getPoint() < 5) {
                throw new TestException(ErrorCode.NOT_ENOUGH_POINT);
            }

            for(int i=1; i<=6; i++) { // 카테고리
                for(int j=1; j<=3; j++) { // 레벨
                    Problem problem = problemTestRepository.findFormalProblemByParentCategoryAndLevel(i, j)
                            .orElseThrow(() -> new ProblemNotFoundException(ErrorCode.PROBLEM_NOT_FOUND));

                    problemList.add(problemResponseDto(problem));
                }
            }

        }

        return problemList;
    }

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

    // 문제를 응답하는 private 메소드
    private ProblemResponseDto problemResponseDto(Problem problem) {
        // 존재 하지 않는 카테고리나 응답 인 경우에 예외 발생 처리하기
        Category category = categoryRepository.findById(problem.getCategoryId())
                .orElseThrow(() -> new NoSuchCategoryException(ErrorCode.CATEGORY_NOT_FOUND));

        AnswerType answerType = answerTypeRepository.findById(problem.getAnswerTypeId())
                .orElseThrow(() -> new NoSuchAnswerTypeException(ErrorCode.ANSWER_TYPE_NOT_FOUND));

        return ProblemResponseDto.builder()
                .problemId(problem.getProblemId())
                .categoryId(problem.getCategoryId())
                .categoryName(category.getName())
                .answerTypeId(problem.getAnswerTypeId())
                .answerTypeDescription(answerType.getDescription())
                .contentImageUrl(problem.getContentImageUrl())
                .level(problem.getLevel())
                .timeLimit(category.getTimeLimit())
                .build();
    }

}