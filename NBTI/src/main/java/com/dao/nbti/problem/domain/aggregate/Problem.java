package com.dao.nbti.problem.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "problem")
@Getter
@NoArgsConstructor
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int problemId;
    private Integer categoryId;
    private Integer answerTypeId;
    private String contentImageUrl;
    private String correctAnswer;
    @Enumerated(EnumType.STRING)
    private IsDeleted isDeleted;
    private int level;
}
