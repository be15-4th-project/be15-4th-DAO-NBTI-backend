package com.dao.nbti.test.domain.repository;

import com.dao.nbti.test.domain.aggregate.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepository extends JpaRepository<TestResult, Integer> {

}
