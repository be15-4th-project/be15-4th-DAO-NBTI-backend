package com.dao.nbti.problem.application.dto.response;

import com.dao.nbti.problem.domain.aggregate.IsDeleted;
import lombok.Getter;

@Getter
public class ProblemSummaryDTO {
    private int problemId;
    private Integer categoryId;
    private Integer answerTypeId;
    private IsDeleted isDeleted;
    private int level;


    public ProblemSummaryDTO(int problemId, Integer categoryId, int level) {
        this.problemId = problemId;
        this.categoryId = categoryId;
        this.level = level;
    }
}
