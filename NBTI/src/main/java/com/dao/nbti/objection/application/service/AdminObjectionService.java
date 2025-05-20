package com.dao.nbti.objection.application.service;

import com.dao.nbti.common.dto.Pagination;
import com.dao.nbti.objection.application.dto.request.AdminObjectionSearchRequest;
import com.dao.nbti.objection.application.dto.response.AdminObjectionDTO;
import com.dao.nbti.objection.application.dto.response.AdminObjectionListResponse;
import com.dao.nbti.objection.domain.repository.ObjectionRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminObjectionService {
    private final ObjectionRepositoryCustom objectionRepositoryCustom;

    @Transactional(readOnly = true)
    public AdminObjectionListResponse getObjections(AdminObjectionSearchRequest adminObjectionSearchRequest) {
        List<AdminObjectionDTO> objections = objectionRepositoryCustom.getObjections(adminObjectionSearchRequest);

        long totalItems = objectionRepositoryCustom.countObjectionsBy(adminObjectionSearchRequest);

        return AdminObjectionListResponse.builder()
                .objections(objections)
                .pagination(Pagination.builder()
                                .currentPage(adminObjectionSearchRequest.getPage())
                                .totalItems(totalItems)
                                .totalPage((int) Math.ceil((double) totalItems / adminObjectionSearchRequest.getSize()))
                                .build())
                .build();
    }
}
