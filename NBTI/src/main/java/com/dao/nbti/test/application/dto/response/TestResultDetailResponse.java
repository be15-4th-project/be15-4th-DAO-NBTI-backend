package com.dao.nbti.test.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TestResultDetailResponse {
    private List<CategoryScoreDetail> scores;
    private int langComp;
    private int generalKnowledge;
    private int percReason;
    private int workMemory;
    private int procSpeed;
    private int spatialPerception;
    private String aiText;
    private LocalDateTime createdAt;
}

