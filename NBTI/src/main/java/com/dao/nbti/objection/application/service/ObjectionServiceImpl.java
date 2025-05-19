package com.dao.nbti.objection.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.objection.application.dto.response.ObjectionDetailResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionSummaryResponse;
import com.dao.nbti.objection.domain.aggregate.Objection;
import com.dao.nbti.objection.domain.aggregate.Status;
import com.dao.nbti.objection.domain.repository.ObjectionRepository;
import com.dao.nbti.objection.exception.ObjectionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectionServiceImpl implements ObjectionService {

    private final ObjectionRepository objectionRepository;

    @Override
    public List<ObjectionSummaryResponse> getObjectionsByUser(int userId, Status status) {
        List<Objection> objections = (status == null)
                ? objectionRepository.findByUserId(userId)
                : objectionRepository.findByUserIdAndStatus(userId, status);

        return objections.stream().map(o -> ObjectionSummaryResponse.builder()
                .objectionId(o.getObjectionId())
                .problemId(o.getProblemId())
                .status(o.getStatus())
                .createdAt(o.getCreatedAt())
                .build()
        ).toList();
    }

}
