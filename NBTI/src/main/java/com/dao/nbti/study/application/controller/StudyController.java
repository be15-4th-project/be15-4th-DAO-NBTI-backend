package com.dao.nbti.study.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.study.application.dto.response.ProblemResponseDto;
import com.dao.nbti.study.application.service.StudyService;
import com.dao.nbti.study.exception.NoSuchAnswerTypeException;
import com.dao.nbti.study.exception.NoSuchCategoryException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
@Tag(name = "학습", description = "학습 문제, 기록, 결과 조회")
public class StudyController {

    private final StudyService studyService;

    @GetMapping
    @Operation(summary = "학습 문제 3개 제공", description = "선택한 분야와 난이도에 따라 3문제를 제공합니다. level=0이면 무작위 난이도입니다.")
    public ResponseEntity<ApiResponse<List<ProblemResponseDto>>> getStudyProblems(
            @RequestParam Integer categoryId,
            @RequestParam int level
    ) {
        List<ProblemResponseDto> problems = studyService.getProblems(categoryId, level);
        return ResponseEntity.ok(ApiResponse.success(problems));
    }

    @ExceptionHandler(NoSuchCategoryException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchCategoryException(NoSuchCategoryException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

    @ExceptionHandler(NoSuchAnswerTypeException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoSuchAnswerTypeException(NoSuchAnswerTypeException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }
}
