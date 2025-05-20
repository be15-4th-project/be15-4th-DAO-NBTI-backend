package com.dao.nbti.test.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;
import com.dao.nbti.test.application.service.TestService;
import com.dao.nbti.user.domain.aggregate.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Tag(name = "검사", description = "검사 문제 조회, 결과 저장, 결과 조회")
public class TestController {

    private final TestService testService;

    /* 검사 결과 저장하기 */
    @PostMapping("/result")
    @Operation(
            summary = "지능 검사 결과 저장하기", description = "지능 검사 결과를 저장합니다."
    )
    public ResponseEntity<ApiResponse<Void>> createTestResult(
            @RequestBody @Validated TestResultCreateRequest testResultCreateRequest,
            @AuthenticationPrincipal User user
    ){

        Integer userId = (user != null) ? user.getUserId() : null;

        testService.createTestResult(testResultCreateRequest, userId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 검사 결과 마이페이지에 저장하기 */
    @PutMapping("/result/my-page")
    @Operation(
            summary = "지능 검사 결과 마이페이지에 저장하기", description = "회원의 지능 검사 결과를 마이페이지에 저장합니다."
    )
    public ResponseEntity<ApiResponse<Void>> updateTestResult(
            @AuthenticationPrincipal User user
    ){

        // 로그인 된 회원만 바꿀 수 있음
        testService.updateTestResult(user.getUserId());

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}