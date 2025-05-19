package com.dao.nbti.problem.application.service;

import com.dao.nbti.common.dto.Pagination;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.application.dto.request.ProblemSearchRequest;
import com.dao.nbti.problem.application.dto.response.ProblemDTO;
import com.dao.nbti.problem.application.dto.response.ProblemDetailsResponse;
import com.dao.nbti.problem.application.dto.response.ProblemListResponse;
import com.dao.nbti.problem.application.dto.response.ProblemSummaryDTO;
import com.dao.nbti.problem.domain.aggregate.Category;
import com.dao.nbti.problem.domain.aggregate.Problem;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.problem.domain.repository.ProblemRepository;
import com.dao.nbti.problem.domain.repository.ProblemRepositoryCustom;
import com.dao.nbti.problem.exception.ProblemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProblemService {
    private final ProblemRepositoryCustom problemRepositoryCustom;
    private final ProblemRepository problemRepository;
    private final CategoryRepository categoryRepository;

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

    public ProblemDetailsResponse getProblemDetails(int problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemException(ErrorCode.PROBLEM_NOT_FOUND));
        Category category = categoryRepository.findById(problem.getCategoryId())
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));
        Category parentCategory = categoryRepository.findById(category.getParentCategoryId())
                .orElseThrow(() -> new ProblemException(ErrorCode.CATEGORY_NOT_FOUND));

        String childCategoryName = category.getName();
        String parentCategoryName = parentCategory.getName();

        ProblemDTO problemDTO = ProblemDTO.builder()
                .problemId(problem.getProblemId())
                .categoryId(problem.getProblemId())
                .parentCategoryName(parentCategoryName)
                .childCategoryName(childCategoryName)
                .answerTypeId(problem.getAnswerTypeId())
                .contentImageUrl(problem.getContentImageUrl())
                .correctAnswer(problem.getCorrectAnswer())
                .level(problem.getLevel())
                .build();

        return ProblemDetailsResponse.builder()
                .problem(problemDTO)
                .build();
    }
}
