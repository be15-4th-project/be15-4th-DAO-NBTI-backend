package com.dao.nbti.problem.domain.repository;

import com.dao.nbti.problem.domain.aggregate.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
