package com.dao.nbti.objection.domain.repository;

import com.dao.nbti.objection.domain.aggregate.Objection;
import com.dao.nbti.objection.domain.aggregate.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ObjectionRepository extends JpaRepository<Objection, Integer> {

    // 사용자 ID로 전체 이의 제기 목록 조회
    List<Objection> findByUserId(int userId);

    // 사용자 ID + 상태로 필터링된 목록 조회
    List<Objection> findByUserIdAndStatus(int userId, Status status);

    // 특정 사용자의 이의 제기 상세 조회 (소유자 검증 포함)
    Optional<Objection> findByObjectionIdAndUserId(int objectionId, int userId);
}