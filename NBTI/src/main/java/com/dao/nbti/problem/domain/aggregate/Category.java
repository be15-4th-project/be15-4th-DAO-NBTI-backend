package com.dao.nbti.problem.domain.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    private Integer parentCategoryId;
    @NotNull
    @NotBlank
    private String name;
    private String description;
    private int timeLimit;
}
