package com.dao.nbti.study.domain.repository;

import com.dao.nbti.study.domain.aggregate.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Integer> {
}
