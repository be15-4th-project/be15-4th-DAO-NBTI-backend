package com.dao.nbti.common.auth.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordResetRequest {
    @NotBlank
    private String password;
    private String verifiedPassword;
}
