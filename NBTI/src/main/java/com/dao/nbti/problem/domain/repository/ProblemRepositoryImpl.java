package com.dao.nbti.problem.domain.repository;

import com.dao.nbti.problem.application.dto.request.ProblemSearchRequest;
import com.dao.nbti.problem.application.dto.response.ProblemSummaryDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProblemRepositoryImpl implements ProblemRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProblemSummaryDTO> getProblemsBy(ProblemSearchRequest request) {
        String initQuery = """
                SELECT new com.dao.nbti.problem.application.dto.response.ProblemSummaryDTO(
                p.problemId, p.categoryId, p.level
                )
                FROM Problem p
                WHERE p.isDeleted=com.dao.nbti.problem.domain.aggregate.IsDeleted.N
                """;
        StringBuilder jpql = getDynamicQueryBuilder(initQuery, request);

        TypedQuery<ProblemSummaryDTO> query = em.createQuery(jpql.toString(), ProblemSummaryDTO.class);

        if (request.getChildCategoryId() != null) {
            query.setParameter("categoryId", request.getChildCategoryId());
        }
        if (request.getLevel() != null) {
            query.setParameter("level", request.getLevel());
        }

        return query
                .setFirstResult(request.getOffset())
                .setMaxResults(request.getLimit())
                .getResultList();
    }

    @Override
    public int countProblemsBy(ProblemSearchRequest request) {
        String initQuery = """
                SELECT COUNT(p)
                FROM Problem p
                WHERE p.isDeleted=com.dao.nbti.problem.domain.aggregate.IsDeleted.N
                """;
        StringBuilder jpql = getDynamicQueryBuilder(initQuery, request);

        TypedQuery<Integer> query = em.createQuery(jpql.toString(), Integer.class);

        if (request.getChildCategoryId() != null) {
            query.setParameter("categoryId", request.getChildCategoryId());
        }
        if (request.getLevel() != null) {
            query.setParameter("level", request.getLevel());
        }

        return query.getSingleResult();
    }

    private static StringBuilder getDynamicQueryBuilder(String str, ProblemSearchRequest request) {
        StringBuilder jpql = new StringBuilder(str);

        if (request.getChildCategoryId() != null) {
            jpql.append("\nAND p.categoryId = :categoryId");
        }
        if (request.getLevel() != null) {
            jpql.append("\nAND p.level = :level");
        }
        return jpql;
    }
}
