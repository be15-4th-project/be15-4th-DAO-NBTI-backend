package com.dao.nbti.objection.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionDetailResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionSummaryResponse;
import com.dao.nbti.objection.application.service.ObjectionService;
import com.dao.nbti.objection.domain.aggregate.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mypage/objections")
@RequiredArgsConstructor
@Tag(name = "이의 제기", description = "사용자 이의 제기 조회 API")
public class ObjectionController {

    private final ObjectionService objectionService;

    @Operation(summary = "이의 제기 목록 조회", description = "로그인한 사용자의 이의 제기 내역을 상태별로 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ObjectionSummaryResponse>>> getObjections(
            @Parameter(description = "이의 제기 상태 필터", example = "PENDING")
            @RequestParam(required = false) Status status,

            @Parameter(hidden = true, description = "JWT 인증된 사용자 ID")
            @AuthenticationPrincipal(expression = "userId") int userId
    ) {
        List<ObjectionSummaryResponse> list = objectionService.getObjectionsByUser(userId, status);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @Operation(summary = "이의 제기 상세 조회", description = "선택한 이의 제기 항목의 상세 정보를 조회합니다.")
    @GetMapping("/{objectionId}")
    public ResponseEntity<ApiResponse<ObjectionDetailResponse>> getObjectionDetail(
            @Parameter(description = "조회할 이의 제기 ID", example = "7")
            @PathVariable int objectionId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal(expression = "userId") int userId
    ) {
        ObjectionDetailResponse detail = objectionService.getObjectionDetail(objectionId, userId);
        return ResponseEntity.ok(ApiResponse.success(detail));
    }
}
