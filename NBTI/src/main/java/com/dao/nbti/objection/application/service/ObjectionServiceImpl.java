package com.dao.nbti.objection.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.objection.application.dto.request.ObjectionCreateRequest;
import com.dao.nbti.objection.application.dto.response.ObjectionCreateResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionDetailResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionSummaryResponse;
import com.dao.nbti.objection.domain.aggregate.Objection;
import com.dao.nbti.objection.domain.aggregate.Status;
import com.dao.nbti.objection.domain.repository.ObjectionRepository;
import com.dao.nbti.objection.exception.ObjectionException;
import com.dao.nbti.problem.domain.aggregate.IsDeleted;
import com.dao.nbti.problem.domain.aggregate.Problem;
import com.dao.nbti.problem.domain.repository.ProblemRepository;
import com.dao.nbti.study.domain.repository.StudyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectionServiceImpl implements ObjectionService {

    private final ObjectionRepository objectionRepository;
    private final ProblemRepository problemRepository;
    private final StudyResultRepository studyResultRepository;

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

    @Override
    public ObjectionDetailResponse getObjectionDetail(int objectionId, int userId) {
        Objection objection = objectionRepository.findByObjectionIdAndUserId(objectionId, userId)
                .orElseThrow(() -> new ObjectionException(ErrorCode.UNAUTHORIZED_TEST_RESULT_ACCESS)); // or OBJECTION_NOT_FOUND

        return ObjectionDetailResponse.builder()
                .objectionId(objection.getObjectionId())
                .problemId(objection.getProblemId())
                .status(objection.getStatus())
                .reason(objection.getReason())
                .information(objection.getInformation())
                .createdAt(objection.getCreatedAt())
                .processedAt(objection.getProcessedAt())
                .build();
    }

    @Override
    public ObjectionCreateResponse createObjection(ObjectionCreateRequest request, int userId) {
        int problemId = request.getProblemId();

        // 1. 문제 존재 여부 및 삭제 여부 확인
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ObjectionException(ErrorCode.PROBLEM_NOT_FOUND));

        if (problem.getIsDeleted() == IsDeleted.Y) {
            throw new ObjectionException(ErrorCode.PROBLEM_DELETED);
        }

        // 2. 중복 이의 제기 방지
        if (objectionRepository.existsByUserIdAndProblemId(userId, problemId)) {
            throw new ObjectionException(ErrorCode.DUPLICATE_OBJECTION);
        }

        // 3. 학습한 문제인지 확인
        if (!studyResultRepository.existsByUserIdAndProblemId(userId, problemId)) {
            throw new ObjectionException(ErrorCode.INVALID_OBJECTION_TARGET);
        }

        // 4. 저장
        Objection objection = Objection.builder()
                .userId(userId)
                .problemId(problemId)
                .status(Status.PENDING)
                .reason(request.getReason())
                .createdAt(LocalDateTime.now())
                .build();

        objectionRepository.save(objection);

        return ObjectionCreateResponse.builder()
                .objectionId(objection.getObjectionId())
                .message("이의 제기가 등록되었습니다.")
                .build();
    }
}
