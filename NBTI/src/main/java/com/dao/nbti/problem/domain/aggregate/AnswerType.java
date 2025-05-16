package com.dao.nbti.problem.domain.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "answer_type")
public class AnswerType {
    @Id
    private int answerTypeId;
    @NotNull
    @NotBlank
    private String description;
}
