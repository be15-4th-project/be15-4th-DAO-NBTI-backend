package com.dao.nbti.problem.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProblemDTO {
    private int problemId;
    private Integer categoryId;
    private String parentCategoryName;
    private String childCategoryName;
    private Integer answerTypeId;
    private String answerTypeDescription;
    private String contentImageUrl;
    private String correctAnswer;
    private int level;

    @Builder
    public ProblemDTO(int problemId, Integer categoryId, String parentCategoryName, String childCategoryName, Integer answerTypeId, String answerTypeDescription, String contentImageUrl, String correctAnswer, int level) {
        this.problemId = problemId;
        this.categoryId = categoryId;
        this.parentCategoryName = parentCategoryName;
        this.childCategoryName = childCategoryName;
        this.answerTypeId = answerTypeId;
        this.answerTypeDescription = answerTypeDescription;
        this.contentImageUrl = contentImageUrl;
        this.correctAnswer = correctAnswer;
        this.level = level;
    }
}
