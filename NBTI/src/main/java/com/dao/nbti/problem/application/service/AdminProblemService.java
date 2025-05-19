package com.dao.nbti.problem.application.service;

import com.dao.nbti.common.dto.Pagination;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.application.dto.request.ProblemCreateRequest;
import com.dao.nbti.problem.application.dto.request.ProblemSearchRequest;
import com.dao.nbti.problem.application.dto.response.ProblemDTO;
import com.dao.nbti.problem.application.dto.response.ProblemDetailsResponse;
import com.dao.nbti.problem.application.dto.response.ProblemListResponse;
import com.dao.nbti.problem.application.dto.response.ProblemSummaryDTO;
import com.dao.nbti.problem.domain.aggregate.*;
import com.dao.nbti.problem.domain.repository.AnswerTypeRepository;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.problem.domain.repository.ProblemRepository;
import com.dao.nbti.problem.domain.repository.ProblemRepositoryCustom;
import com.dao.nbti.problem.exception.ProblemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProblemService {
    private final ProblemRepositoryCustom problemRepositoryCustom;
    private final ProblemRepository problemRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerTypeRepository answerTypeRepository;

    @Transactional(readOnly = true)
    public ProblemListResponse getProblems(ProblemSearchRequest problemSearchRequest) {
        List<ProblemSummaryDTO> problems = problemRepositoryCustom.getProblemsBy(problemSearchRequest);

        long totalItems = problemRepositoryCustom.countProblemsBy(problemSearchRequest);

        return ProblemListResponse.builder()
                .problems(problems)
                .pagination(Pagination.builder()
                        .currentPage(problemSearchRequest.getPage())
                        .totalItems(totalItems)
                        .totalPage((int) Math.ceil((double) totalItems / problemSearchRequest.getSize()))
                        .build()
                ).build();
    }

    @Transactional(readOnly = true)
    public ProblemDetailsResponse getProblemDetails(int problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemException(ErrorCode.PROBLEM_NOT_FOUND));
        Category category = categoryRepository.findById(problem.getCategoryId())
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));
        Category parentCategory = categoryRepository.findById(category.getParentCategoryId())
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));
        AnswerType answerType = answerTypeRepository.findById(problem.getAnswerTypeId())
                .orElseThrow(() -> new ProblemException(ErrorCode.ANSWER_TYPE_NOT_FOUND));

        String childCategoryName = category.getName();
        String parentCategoryName = parentCategory.getName();
        String answerTypeDescription = AnswerTypeEnum.of(answerType.getAnswerTypeId());

        ProblemDTO problemDTO = ProblemDTO.builder()
                .problemId(problem.getProblemId())
                .categoryId(problem.getProblemId())
                .parentCategoryName(parentCategoryName)
                .childCategoryName(childCategoryName)
                .answerTypeId(problem.getAnswerTypeId())
                .answerTypeDescription(answerTypeDescription)
                .contentImageUrl(problem.getContentImageUrl())
                .correctAnswer(problem.getCorrectAnswer())
                .level(problem.getLevel())
                .build();

        return ProblemDetailsResponse.builder()
                .problem(problemDTO)
                .build();
    }

    // 'https://a3.nbti.ai/images/problem_lang_07_01.png'
    @Transactional
    public ProblemDetailsResponse createProblem(ProblemCreateRequest problemCreateRequest) {
        int categoryId = problemCreateRequest.getCategoryId();
        int answerTypeId = problemCreateRequest.getAnswerTypeId();
        String contentImageUrl = problemCreateRequest.getContentImageUrl();
        String correctAnswer = problemCreateRequest.getCorrectAnswer();
        int level = problemCreateRequest.getLevel();

        validateProblemCreateRequest(problemCreateRequest);

        Problem problem = Problem.builder()
                .categoryId(categoryId)
                .answerTypeId(answerTypeId)
                .contentImageUrl(contentImageUrl)
                .correctAnswer(correctAnswer)
                .isDeleted(IsDeleted.N)
                .level(level)
                .build();

        Problem saved = problemRepository.save(problem);
        String answerTypeDescription = AnswerTypeEnum.of(problemCreateRequest.getAnswerTypeId());

        Category childCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));
        Category parentCategory = categoryRepository.findById(childCategory.getParentCategoryId())
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));

        ProblemDTO problemDTO = ProblemDTO.builder()
                .problemId(saved.getProblemId())
                .categoryId(categoryId)
                .parentCategoryName(parentCategory.getName())
                .childCategoryName(childCategory.getName())
                .answerTypeId(answerTypeId)
                .answerTypeDescription(answerTypeDescription)
                .contentImageUrl(contentImageUrl)
                .correctAnswer(correctAnswer)
                .level(level)
                .build();

        return ProblemDetailsResponse.builder()
                .problem(problemDTO)
                .build();
    }

    private void validateProblemCreateRequest(ProblemCreateRequest problemCreateRequest) {
        int categoryId = problemCreateRequest.getCategoryId();
        int answerTypeId = problemCreateRequest.getAnswerTypeId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));

        /* parentCategoryId == null일 때 발생하는 500 에러를 커스텀 에러로 전환 */
        Integer parentCategoryId = category.getParentCategoryId();
        if (parentCategoryId == null) {
            throw new ProblemException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (!categoryRepository.existsById(categoryId)) {
            throw new ProblemException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (!answerTypeRepository.existsById(answerTypeId)) {
            throw new ProblemException(ErrorCode.ANSWER_TYPE_NOT_FOUND);
        }
    }
}
