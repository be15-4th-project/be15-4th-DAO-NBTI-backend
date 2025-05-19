package com.dao.nbti.common.auth.application.controller;

import com.dao.nbti.common.auth.application.dto.LoginRequest;
import com.dao.nbti.common.auth.application.dto.LoginResponse;
import com.dao.nbti.common.auth.application.dto.TokenResponse;
import com.dao.nbti.common.auth.application.service.AuthService;
import com.dao.nbti.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @GetMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest){
        LoginResponse response = authService.login(loginRequest);
        ResponseCookie cookie = createRefreshTokenCookie(response.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(response));
    }


    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // refreshToken이 없으면 401 반환
        }
        TokenResponse tokenResponse = authService.refreshToken(refreshToken);
        ResponseCookie cookie = createRefreshTokenCookie(tokenResponse.getRefreshToken());  // refreshToken 쿠키 생성
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(tokenResponse));
    }

    private ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                // .secure(true) // 운영 환경에서 HTTPS 사용 시 활성화
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
    }
}
