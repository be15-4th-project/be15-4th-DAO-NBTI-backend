package com.dao.nbti.problem.domain.repository;

import com.dao.nbti.problem.domain.aggregate.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Integer> {
}
