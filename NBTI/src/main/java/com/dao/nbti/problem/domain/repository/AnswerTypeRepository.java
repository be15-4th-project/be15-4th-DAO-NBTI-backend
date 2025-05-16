package com.dao.nbti.problem.domain.repository;

import com.dao.nbti.problem.domain.aggregate.AnswerType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerTypeRepository extends JpaRepository<AnswerType, Integer> {
}
