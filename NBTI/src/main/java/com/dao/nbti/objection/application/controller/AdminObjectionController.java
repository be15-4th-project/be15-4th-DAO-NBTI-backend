package com.dao.nbti.objection.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.objection.application.dto.request.AdminObjectionSearchRequest;
import com.dao.nbti.objection.application.dto.response.AdminObjectionListResponse;
import com.dao.nbti.objection.application.dto.response.ObjectionSummaryResponse;
import com.dao.nbti.objection.application.service.AdminObjectionService;
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
@RequestMapping("/admin/objections")
@RequiredArgsConstructor
@Tag(name = "이의 제기 관리", description = "관리자 이의 제기 관리 API")
public class AdminObjectionController {

    private final AdminObjectionService adminObjectionService;

    @Operation(summary = "이의 제기 목록 조회", description = "관리자가 이의 제기 목록을 조회합니다. 회원 아이디, 문제 ID, 상태로 필터링할 수 있습니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<AdminObjectionListResponse>> getObjections(
            @ModelAttribute AdminObjectionSearchRequest adminObjectionSearchRequest
            ) {
        AdminObjectionListResponse objections = adminObjectionService.getObjections(adminObjectionSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(objections));
    }
}
