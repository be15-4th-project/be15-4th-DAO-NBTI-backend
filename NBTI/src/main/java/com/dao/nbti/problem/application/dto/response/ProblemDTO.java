package com.dao.nbti.problem.application.dto.response;

import com.dao.nbti.problem.domain.aggregate.IsDeleted;

public class ProblemDTO {
    private int problemId;
    private Integer categoryId;
    private Integer answerTypeId;
    private String contentImageUrl;
    private String correctAnswer;
    private IsDeleted isDeleted;
    private int level;
}
