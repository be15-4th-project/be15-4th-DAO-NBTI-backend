package com.dao.nbti.objection.domain.repository;

import com.dao.nbti.objection.domain.aggregate.Objection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectionRepository extends JpaRepository<Objection, Integer> {
}
