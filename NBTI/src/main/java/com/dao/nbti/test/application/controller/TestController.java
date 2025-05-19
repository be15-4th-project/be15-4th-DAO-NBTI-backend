package com.dao.nbti.test.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.test.application.dto.request.AiAnswerCreateRequest;
import com.dao.nbti.test.application.dto.response.AiAnswerResponse;
import com.dao.nbti.test.application.service.TestAiAnswerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Tag(name = "검사", description = "검사 문제 조회, 결과 저장, 결과 조회")
public class TestController {

    private final TestAiAnswerService testAiAnswerService;

    /* ai 분석 생성하기 */
    @PostMapping("/ai/answer")
    @Operation(
            summary = "지능 검사 ai 분석 생성하기", description = "chap gpt 지능 검사 분석 결과 생성"
    )
    public ResponseEntity<ApiResponse<AiAnswerResponse>> getAiAnswer(
            @RequestBody @Validated  AiAnswerCreateRequest request
    ) {
        AiAnswerResponse response = testAiAnswerService.getAiAnswer(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

