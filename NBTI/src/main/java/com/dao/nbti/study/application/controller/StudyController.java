package com.dao.nbti.study.application.controller;

import com.dao.nbti.study.application.service.StudyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
@Tag(name = "학습", description = "학습 문제, 기록, 결과 조회")
public class StudyController {

    private final StudyService studyService;
}
