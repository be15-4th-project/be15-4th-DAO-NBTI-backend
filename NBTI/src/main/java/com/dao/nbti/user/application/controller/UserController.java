package com.dao.nbti.user.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.user.application.dto.IdDuplicateResponse;
import com.dao.nbti.user.application.dto.UserCreateRequest;
import com.dao.nbti.user.application.service.UserService;
import com.dao.nbti.user.exception.UserException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @Operation(summary = "회원가입", description = "사용자는 사용자 정보를 입력하여 회원가입 할 수 있다.")
    @GetMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody UserCreateRequest userCreateRequest){
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 시 데이터 익명화 및 계정 비활성화 처리한다.")
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(@AuthenticationPrincipal User user){
        Integer userId = Integer.parseInt(user.getUsername());
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "아이디 중복 확인", description = "회원가입을 할 경우 해당 아이디가 서비스에 등록되어 있는지 확인한다.")
    @GetMapping("/id-duplicate")
    public ResponseEntity<ApiResponse<IdDuplicateResponse>> idDuplicate(@RequestParam String accountId){
        IdDuplicateResponse response = userService.checkAccountId(accountId);

        return ResponseEntity.ok(ApiResponse.success(response));
    }


    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserException(UserException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.failure(errorCode.getCode(), errorCode.getMessage()));
    }

}
