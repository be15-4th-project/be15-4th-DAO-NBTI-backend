package com.dao.nbti.test.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.common.dto.Pagination;
import com.dao.nbti.test.application.dto.request.AdminTestResultSearchCondition;
import com.dao.nbti.test.application.dto.response.AdminTestResultSummaryResponse;
import com.dao.nbti.test.application.dto.response.TestResultDetailResponse;
import com.dao.nbti.test.application.dto.request.TestResultSearchCondition;
import com.dao.nbti.test.application.dto.response.TestResultSummaryResponse;
import com.dao.nbti.test.application.service.TestResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test-result")
@Tag(name = "검사 결과", description = "검사 결과 조회 API")
public class TestResultController {

    private final TestResultService testResultService;

    @Operation(summary = "검사 결과 목록 조회", description = "로그인한 사용자의 검사 결과 목록을 조회합니다. 연도/월별 필터링 및 페이지네이션이 가능합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTestResults(
            @Parameter(hidden = true, description = "JWT 인증된 사용자 ID")
            @AuthenticationPrincipal(expression = "userId") int userId,

            @Parameter(description = "조회할 연도", example = "2025")
            @RequestParam(required = false) Integer year,

            @Parameter(description = "조회할 월 (1~12)", example = "5")
            @RequestParam(required = false) Integer month,

            @Parameter(description = "정렬 기준 (recent: 최신순, past: 과거순)", example = "recent")
            @RequestParam(defaultValue = "recent") String sort,

            @Parameter(description = "페이지 및 정렬 정보")
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        TestResultSearchCondition condition = new TestResultSearchCondition(year, month, sort, userId);
        Page<TestResultSummaryResponse> resultPage = testResultService.getTestResultList(condition, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", resultPage.getContent());
        response.put("pagination", Pagination.builder()
                .currentPage(resultPage.getNumber() + 1)
                .totalPage(resultPage.getTotalPages())
                .totalItems(resultPage.getTotalElements())
                .build());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "검사 결과 상세 조회", description = "검사 결과 ID를 통해 상세 정보를 조회합니다.")
    @GetMapping("/{testResultId}")
    public ResponseEntity<ApiResponse<TestResultDetailResponse>> getTestResultDetail(
            @Parameter(hidden = true, description = "JWT 인증된 사용자 ID")
            @AuthenticationPrincipal(expression = "userId") int userId,

            @Parameter(description = "검사 결과 ID", example = "1")
            @PathVariable int testResultId
    ) {
        TestResultDetailResponse detail = testResultService.getTestResultDetail(testResultId, userId);
        return ResponseEntity.ok(ApiResponse.success(detail));
    }

    @Operation(summary = "검사 목록 조회", description = "검사 결과 ID를 통해 상세 정보를 조회합니다.")
    @GetMapping("/list/admin")
    public ResponseEntity<ApiResponse<Map<String,Object>>> getTestResults(
            @Parameter(description = "사용자 id", example="user01")
            @RequestParam(required = false) String accountId,

            @Parameter(description = "조회할 연도", example = "2025")
            @RequestParam(required = false) Integer year,

            @Parameter(description = "조회할 월 (1~12)", example = "5")
            @RequestParam(required = false) Integer month,

            @Parameter(description = "페이지 및 정렬 정보")
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        AdminTestResultSearchCondition condition = new AdminTestResultSearchCondition(year, month, accountId);

        AdminTestResultSummaryResponse resultPage = testResultService.getAdminTestResultList(condition, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", resultPage.getPage().getContent());
        response.put("pagination", Pagination.builder()
                .currentPage(resultPage.getPage().getNumber() + 1)
                .totalPage(resultPage.getPage().getTotalPages())
                .totalItems(resultPage.getPage().getTotalElements())
                .build());

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
