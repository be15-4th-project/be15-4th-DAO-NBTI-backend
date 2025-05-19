package com.dao.nbti.problem.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.problem.application.dto.request.ProblemSearchRequest;
import com.dao.nbti.problem.application.dto.response.ProblemListResponse;
import com.dao.nbti.problem.application.service.AdminProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/problems")
@RequiredArgsConstructor
@Tag(name = "문제 관리", description = "관리자 문제 관리 기능 API")
public class AdminProblemController {

    private final AdminProblemService adminProblemService;

    @GetMapping("/list")
    @Operation(summary = "문제 목록 조회", description = "관리자가 서비스에 등록된 전체 또는 필터링된 문제 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<ProblemListResponse>> getProblems(@ModelAttribute ProblemSearchRequest problemSearchRequest) {

        return ResponseEntity.ok(ApiResponse.success(adminProblemService.getProblems(problemSearchRequest)));
    }

}
