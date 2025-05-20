
package com.dao.nbti.test.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.study.application.dto.response.ProblemResponseDto;
import com.dao.nbti.test.application.dto.request.TestResultCreateRequest;
import com.dao.nbti.test.application.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Tag(name = "검사", description = "검사 문제 조회, 결과 저장, 결과 조회")
public class TestController {

    private final TestService testService;

    /* 검사 요청하기 */
    @GetMapping
    @Operation(
            summary = "지능 검사 문제 제공", description = "비회원, 회원 여부에 따라 문제를 제공합니다."
    )
    public ResponseEntity<ApiResponse<List<ProblemResponseDto>>> getTests(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Integer userId = (userDetails != null) ? Integer.parseInt(userDetails.getUsername()) : null;

        List<ProblemResponseDto> problems = testService.getTestProblems(userId);

        return ResponseEntity.ok(ApiResponse.success(problems));
    }

    /* 검사 결과 저장하기 */
    @PostMapping("/result")
    @Operation(
            summary = "지능 검사 결과 저장하기", description = "지능 검사 결과를 저장합니다."
    )
    public ResponseEntity<ApiResponse<Void>> createTestResult(
            @RequestBody @Validated TestResultCreateRequest testResultCreateRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        Integer userId = (userDetails != null) ? Integer.parseInt(userDetails.getUsername()) : null;

        testService.createTestResult(testResultCreateRequest, userId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /* 검사 결과 마이페이지에 저장하기 */
    @PutMapping("/result/my-page")
    @Operation(
            summary = "지능 검사 결과 마이페이지에 저장하기", description = "회원의 지능 검사 결과를 마이페이지에 저장합니다."
    )
    public ResponseEntity<ApiResponse<Void>> updateTestResult(
            @AuthenticationPrincipal UserDetails userDetails
    ){

        // 로그인 된 회원만 바꿀 수 있음
        int userId = Integer.parseInt(userDetails.getUsername());

        testService.updateTestResult(userId);

        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
