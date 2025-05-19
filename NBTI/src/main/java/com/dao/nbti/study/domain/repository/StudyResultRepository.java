package com.dao.nbti.study.domain.repository;

import com.dao.nbti.study.domain.aggregate.StudyResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyResultRepository extends JpaRepository<StudyResult, Integer> {
    boolean existsByUserIdAndProblemId(int userId, int problemId);

}
