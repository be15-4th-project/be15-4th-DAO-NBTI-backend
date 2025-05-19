package com.dao.nbti.study.application.service;

import com.dao.nbti.common.exception.ErrorCode;
import com.dao.nbti.study.application.dto.request.StudySearchCondition;
import com.dao.nbti.study.application.dto.response.StudySummaryResponse;
import com.dao.nbti.study.domain.aggregate.IsCorrect;
import com.dao.nbti.study.domain.aggregate.Study;
import com.dao.nbti.study.domain.repository.StudyRepository;
import com.dao.nbti.study.domain.repository.StudyResultRepository;
import com.dao.nbti.study.exception.StudyException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyResultServiceImpl implements StudyResultService {

    private final StudyRepository studyRepository;
    private final StudyResultRepository studyResultRepository;

    @Override
    public Page<StudySummaryResponse> getStudyList(StudySearchCondition condition, Pageable pageable) {
        Page<Study> results = studyRepository.findByUserAndCondition(
                condition.getUserId(),
                condition.getYear(),
                condition.getMonth(),
                pageable
        );

        return results.map(study -> {
            int studyId = study.getStudyId();

            int totalCount = studyResultRepository.countByStudyId(studyId);
            int correctCount = studyResultRepository.countByStudyIdAndIsCorrect(studyId, IsCorrect.Y);

            List<String> parentCategories = studyResultRepository.findAllParentCategoryNamesByStudyId(studyId);
            if (parentCategories.isEmpty()) {
                throw new StudyException(ErrorCode.PARENT_CATEGORY_NOT_FOUND);
            }

            String parentCategoryName = parentCategories.get(0);

            return StudySummaryResponse.builder()
                    .studyId(studyId)
                    .parentCategoryName(parentCategoryName)
                    .createdAt(study.getCreatedAt())
                    .correctCount(correctCount)
                    .totalCount(totalCount)
                    .build();
        });
    }
}
