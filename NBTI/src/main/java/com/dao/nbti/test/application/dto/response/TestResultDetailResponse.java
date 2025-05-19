package com.dao.nbti.test.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TestResultDetailResponse {
    private int langComp;
    private int generalKnowledge;
    private int percReason;
    private int workMemory;
    private int procSpeed;
    private int spatialPerception;
    private String aiText;
    private LocalDateTime createdAt;
}
