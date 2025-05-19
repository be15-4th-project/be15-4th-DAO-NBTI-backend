package com.dao.nbti.objection.application.service;

import com.dao.nbti.objection.application.dto.response.ObjectionSummaryResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionDetailResponse;
import com.dao.nbti.objection.domain.aggregate.Status;

import java.util.List;

public interface ObjectionService {

    // 목록 조회
    List<ObjectionSummaryResponse> getObjectionsByUser(int userId, Status status);

}
