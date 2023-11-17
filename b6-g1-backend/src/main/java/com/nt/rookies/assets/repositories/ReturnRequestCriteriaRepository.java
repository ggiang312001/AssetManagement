package com.nt.rookies.assets.repositories;

import com.nt.rookies.assets.dtos.ReturnRequestListDto;
import com.nt.rookies.assets.entities.ReturnRequest;
import com.nt.rookies.assets.entities.ReturnRequestState;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository

public class ReturnRequestCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public ReturnRequestCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<ReturnRequest> findAll(ReturnRequestListDto returnRequestListDto) {
        CriteriaQuery<ReturnRequest> criteriaQuery = criteriaBuilder.createQuery(ReturnRequest.class);
        Root<ReturnRequest> returnRequestRoot = criteriaQuery.from(ReturnRequest.class);

        Predicate predicate = getPredicate(returnRequestListDto, returnRequestRoot);

        criteriaQuery.where(predicate);
        setOrder(returnRequestListDto, criteriaQuery, returnRequestRoot);

        TypedQuery<ReturnRequest> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult((returnRequestListDto.getPageNo() - 1) * returnRequestListDto.getPageSize());
        typedQuery.setMaxResults(returnRequestListDto.getPageSize());

        Pageable pageable = getPageable(returnRequestListDto);

        long returnRequestCount = getReturnRequestCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, returnRequestCount);
    }

    private Predicate getPredicate(ReturnRequestListDto returnRequestListDto, Root<ReturnRequest> returnRequestRoot
    ) {
        List<Predicate> searchTermPredicates = new ArrayList<>();
        List<Predicate> returnedDatePredicates = new ArrayList<>();
        List<Predicate> statePredicates = new ArrayList<>();

        String searchTerm = returnRequestListDto.getSearchTerm();

        if (searchTerm != null && !searchTerm.isEmpty() && !searchTerm.isBlank()) {
            Predicate assetNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(returnRequestRoot.get("assignmentId").get("assetId").get("name")),
                    "%" + returnRequestListDto.getSearchTerm().trim().toLowerCase() + "%");

            Predicate assetIdPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(returnRequestRoot.get("assignmentId").get("assetId").get("assetId")),
                    "%" + returnRequestListDto.getSearchTerm().trim().toLowerCase() + "%"
            );
            Predicate usernamePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(returnRequestRoot.get("createdBy").get("username")),
                    "%" + returnRequestListDto.getSearchTerm().trim().toLowerCase() + "%"
            );
            searchTermPredicates.add(assetNamePredicate);
            searchTermPredicates.add(assetIdPredicate);
            searchTermPredicates.add(usernamePredicate);

        }

        String returnedDate = returnRequestListDto.getReturnedDate();
        if (returnedDate != null && !returnedDate.isEmpty() && !returnedDate.isBlank()) {
            LocalDateTime startOfDay = LocalDate.parse(returnedDate.trim()).atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
            returnedDatePredicates.add(
                    criteriaBuilder.between(
                            returnRequestRoot.get("returnedDate"), startOfDay, endOfDay
                    )
            );
        }

        String state = returnRequestListDto.getState();
        if (state != null && !state.isEmpty() && !state.isBlank()) {
            statePredicates.add(
                    criteriaBuilder.equal(
                            returnRequestRoot.get("state").as(String.class),
                            state
                    )
            );
        }

        Predicate excludeRejectedPredicate = criteriaBuilder.notEqual(
                returnRequestRoot.get("state").as(String.class),
                ReturnRequestState.REJECT.toString()
        );

        Predicate searchTermPredicate = criteriaBuilder.or(searchTermPredicates.toArray(new Predicate[0]));
        Predicate returnedDatePredicate = criteriaBuilder.and(returnedDatePredicates.toArray(new Predicate[0]));
        Predicate statePredicate = criteriaBuilder.and(statePredicates.toArray(new Predicate[0]));

        if (searchTerm == null || searchTerm.isEmpty() || searchTerm.isBlank()) {
            return criteriaBuilder.and(returnedDatePredicate, statePredicate, excludeRejectedPredicate);
        }

        return criteriaBuilder.and(searchTermPredicate, returnedDatePredicate, statePredicate, excludeRejectedPredicate);

    }

    private void setOrder(ReturnRequestListDto returnRequestListDto, CriteriaQuery<ReturnRequest> criteriaQuery, Root<ReturnRequest> returnRequestRoot) {

        String sortBy = returnRequestListDto.getSortBy();
        Path<String> fieldToSort;

        if (sortBy.equalsIgnoreCase("assetId")
                || sortBy.equalsIgnoreCase("assetName")) {
            fieldToSort = returnRequestRoot.get("assignmentId").get("assetId").get("assetId");
        } else if (sortBy.equalsIgnoreCase("assignDate")) {
            fieldToSort = returnRequestRoot.get("assignmentId").get("assignDate");
        } else if (sortBy.equalsIgnoreCase("createdBy")) {
            fieldToSort = returnRequestRoot.get("createdBy").get("username");
        } else {
            fieldToSort = returnRequestRoot.get(sortBy);
        }

        if (returnRequestListDto.getSortDir().equalsIgnoreCase("asc")) {
            criteriaQuery.orderBy(criteriaBuilder.asc(fieldToSort));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(fieldToSort));
        }
    }

    private Pageable getPageable(ReturnRequestListDto returnRequestListDto) {
        Sort sort = Sort.by(returnRequestListDto.getSortDir(), returnRequestListDto.getSortBy());
        return PageRequest.of((returnRequestListDto.getPageNo() - 1), returnRequestListDto.getPageSize(), sort);
    }

    private long getReturnRequestCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<ReturnRequest> countRoot = countQuery.from(ReturnRequest.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
