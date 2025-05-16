package com.dao.nbti.problem.domain.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "category")
public class Category {
    @Id
    private int categoryId;
    private Integer parentCategoryId;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private int timeLimit;
}
