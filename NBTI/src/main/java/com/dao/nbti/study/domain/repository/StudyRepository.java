package com.dao.nbti.study.domain.repository;

import com.dao.nbti.study.domain.aggregate.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Integer> {

    @Query("""
        SELECT s FROM Study s
        WHERE s.userId = :userId
          AND (:year IS NULL OR FUNCTION('YEAR', s.createdAt) = :year)
          AND (:month IS NULL OR FUNCTION('MONTH', s.createdAt) = :month)
    """)
    Page<Study> findByUserAndCondition(
            @Param("userId") int userId,
            @Param("year") Integer year,
            @Param("month") Integer month,
            Pageable pageable
    );
}