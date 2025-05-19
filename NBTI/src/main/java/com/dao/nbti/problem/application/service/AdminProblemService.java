package com.dao.nbti.problem.application.service;

import com.dao.nbti.common.dto.Pagination;
import com.dao.nbti.problem.application.dto.request.ProblemSearchRequest;
import com.dao.nbti.problem.application.dto.response.ProblemListResponse;
import com.dao.nbti.problem.application.dto.response.ProblemSummaryDTO;
import com.dao.nbti.problem.domain.repository.ProblemRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProblemService {
    private final ProblemRepositoryCustom problemRepositoryCustom;

    public ProblemListResponse getProblems(ProblemSearchRequest problemSearchRequest) {
        List<ProblemSummaryDTO> problems = problemRepositoryCustom.getProblemsBy(problemSearchRequest);

        int totalItems = problemRepositoryCustom.countProblemsBy(problemSearchRequest);

        return ProblemListResponse.builder()
                .problems(problems)
                .pagination(Pagination.builder()
                        .currentPage(problemSearchRequest.getPage())
                        .totalItems(totalItems)
                        .totalPage((int) Math.ceil((double) totalItems / problemSearchRequest.getSize()))
                        .build()
                ).build();
    }
}
