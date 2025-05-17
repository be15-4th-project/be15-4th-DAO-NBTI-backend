package com.dao.nbti.common.auth.application.service;


import com.dao.nbti.common.auth.application.dto.LoginRequest;
import com.dao.nbti.common.auth.application.dto.LoginResponse;
import com.dao.nbti.common.auth.domain.aggregate.RefreshToken;
import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.common.jwt.JwtTokenProvider;
import com.dao.nbti.user.domain.aggregate.User;
import com.dao.nbti.user.domain.repository.UserRepository;
import com.dao.nbti.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, RefreshToken> redisTemplate;

    //각 계층별 메소드 명 작성 기준을 못찾아서 일단 login으로 합니다.
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByAccountIdAndDeletedAtIsNull(loginRequest.getAccountId())
                .orElseThrow(() -> new UserException(ErrorCode.INVALID_CREDENTIALS)
        );

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new UserException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getAuthority().name());
        String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(user.getUserId()), user.getAuthority().name());

        RefreshToken redisRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .build();

        redisTemplate.opsForValue().set(
                String.valueOf(user.getUserId()),
                redisRefreshToken,
                Duration.ofDays(7)
        );

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authority(user.getAuthority())
                .build();
    }
}
