package com.dao.nbti.problem.domain.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "answer_type")
public class AnswerType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int answerTypeId;
    @NotNull
    @NotBlank
    private String description;
}
