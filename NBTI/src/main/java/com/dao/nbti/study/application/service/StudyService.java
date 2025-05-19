package com.dao.nbti.study.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.problem.domain.aggregate.AnswerType;
import com.dao.nbti.problem.domain.aggregate.Category;
import com.dao.nbti.problem.domain.aggregate.Problem;
import com.dao.nbti.problem.domain.repository.AnswerTypeRepository;
import com.dao.nbti.problem.domain.repository.CategoryRepository;
import com.dao.nbti.problem.domain.repository.ProblemRepository;
import com.dao.nbti.study.application.dto.response.ProblemResponseDto;
import com.dao.nbti.study.domain.repository.StudyRepository;
import com.dao.nbti.study.domain.repository.StudyResultRepository;
import com.dao.nbti.study.exception.NoSuchAnswerTypeException;
import com.dao.nbti.study.exception.NoSuchCategoryException;
import com.dao.nbti.study.exception.ProblemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyResultRepository studyResultRepository;
    private final ProblemRepository problemRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerTypeRepository answerTypeRepository;

    @Transactional(readOnly = true)
    public List<ProblemResponseDto> getProblems(Integer parentCategoryId, int level) {
        List<Problem> problems = (level == 0)
                ? problemRepository.findRandomProblemsByParentCategory(parentCategoryId)
                : problemRepository.findRandomProblemsByParentCategoryAndLevel(parentCategoryId, level);

        // 문제가 없을 경우 예외 처리
        if (problems.isEmpty()) {
            throw new ProblemNotFoundException(ErrorCode.PROBLEM_NOT_FOUND);
        }

        return problems.stream().map(p -> {
            Category category = categoryRepository.findById(p.getCategoryId())
                    .orElseThrow(() -> new NoSuchCategoryException(ErrorCode.CATEGORY_NOT_FOUND));
            AnswerType answerType = answerTypeRepository.findById(p.getAnswerTypeId())
                    .orElseThrow(() -> new NoSuchAnswerTypeException(ErrorCode.ANSWER_TYPE_NOT_FOUND));

            return ProblemResponseDto.builder()
                    .problemId(p.getProblemId())
                    .categoryId(p.getCategoryId())
                    .categoryName(category.getName())
                    .answerTypeId(p.getAnswerTypeId())
                    .answerTypeDescription(answerType.getDescription())
                    .contentImageUrl(p.getContentImageUrl())
                    .level(p.getLevel())
                    .timeLimit(category.getTimeLimit())
                    .build();
        }).toList();
    }


}
