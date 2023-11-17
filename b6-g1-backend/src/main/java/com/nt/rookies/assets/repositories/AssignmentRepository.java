package com.nt.rookies.assets.repositories;


import com.nt.rookies.assets.entities.Assignment;
import com.nt.rookies.assets.entities.AssignmentState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends PagingAndSortingRepository<Assignment, Integer> {
    /**
     * select assignment by locationId
     */
    @Query("select  a from Assignment as a  join Asset as s on a.assetId = s.assetId and s.locationId.locationId= ?1 and a.state not like 'DELETED'and a.state not like 'DECLINED'")
    Page<Assignment> findAssignmentsByLocationId(Integer location_id, Pageable pageable);

    /**
     * select assignment by locationId and asset
     */
    @Query("select  a from Assignment as a  join Asset as s on a.assetId = s.assetId and s.locationId.locationId= ?1 and a.state =?2")
    Page<Assignment> findAssignmentByLocationIdAndState(Integer location_id, AssignmentState stateFill, Pageable pageable);

    /**
     * select assignment by locationId and assigned date
     */
    @Query("select  a from Assignment as a  join Asset as s on a.assetId = s.assetId and s.locationId.locationId= ?1 and a.assignDate = ?2 and a.state not like 'DELETED'and a.state not like 'DECLINED'")
    Page<Assignment> findAssignmentByLocationIdAndAssignDate(Integer location_id, LocalDate dateFill, Pageable pageable);

    /**
     * Search by assetId , assetName and assignee
     */
    @Query("select a from Assignment as a  join Asset as s on a.assetId = s.assetId and  a.state not like 'DELETED'and a.state not like 'DECLINED' and" +
            " s.locationId.locationId= ?1  where s.assetId like lower( concat('%',?2,'%')) or s.name like lower( concat('%',?3,'%')) or a.assignee.username like lower( concat('%',?4,'%'))")
    Page<Assignment> searchAssignment(Integer location_id, String searchId, String searchName, String searchUserName, Pageable pageable);

    /**
     * Search by state and date
     */
    @Query("select a from Assignment as a  join Asset as s on a.assetId = s.assetId and a.state not like 'DELETED'and a.state not like 'DECLINED' and " +
            "s.locationId.locationId= ?1 and a.state= ?2 and a.assignDate =?3")
    Page<Assignment> findAssignmentByLocationIdAndStateAndAssignDate(Integer location_id, AssignmentState stateFill, LocalDate dateFill, Pageable pageable);

    /**
     * Search by state and date and search
     */
    @Query("select  a from Assignment as a  join Asset as s on a.assetId = s.assetId and s.locationId.locationId= ?1 and a.state= ?2 and a.state not like 'DELETED'and a.state not like 'DECLINED'and " +
            "a.assignDate = ?3 where s.assetId like lower( concat('%',?4,'%')) " +
            " or s.name like lower( concat('%',?5,'%')) or a.assignee.username like lower( concat('%',?6,'%'))")
    Page<Assignment> findAssignmentByAll(Integer location_id, AssignmentState stateFill, LocalDate dateFill, String searchId, String searchName, String searchUserName, Pageable pageable);

    /**
     * Search by state and searchterm
     */
    @Query("select  a from Assignment as a  join Asset as s on a.assetId = s.assetId and s.locationId.locationId= ?1 and a.state not like 'DELETED'and a.state not like 'DECLINED' and a.state= ?2  " +
            "where s.assetId like lower( concat('%',?3,'%'))  " +
            " or s.name like lower( concat('%',?4,'%')) or a.assignee.username like lower( concat('%',?5,'%'))")
    Page<Assignment> findAssignmentByStateAndSearchTerm(Integer location_id, AssignmentState stateFill, String searchId, String searchName, String searchUserName, Pageable pageable);

    /**
     * Search by date and searchterm
     */
    @Query("select  a from Assignment as a  join Asset as s on a.assetId = s.assetId and s.locationId.locationId= ?1 and a.state not like 'DELETED'and a.state not like 'DECLINED' and a.assignDate= ?2  " +
            "where s.assetId like lower( concat('%',?3,'%')) " +
            " or s.name like lower( concat('%',?4,'%')) or a.assignee.username like lower( concat('%',?5,'%'))")
    Page<Assignment> findAssignmentByDateAndSearchTerm(Integer location_id, LocalDate dateFill, String searchId, String searchName, String searchUserName, Pageable pageable);

    /**
     * find Asignment by AsignmentId
     *
     * @param assignmentId
     * @return Assignment
     */
    public Optional<Assignment> findByAssignmentId(Integer assignmentId);

    /**
     * find all Assignments of a Staff
     *
     * @param staffCode request staff code off staff
     * @param pageable  request paging(page size, page number)
     * @return Page<Assignment>
     */
    @Query("select a from Assignment a where lower(a.assignee.staffCode) like lower(?1) and " +
            "(lower(a.state) like 'WAITING_FOR_ACCEPTANCE' or lower(a.state) like 'ACCEPTED')")
    Page<Assignment> findByAssignee_StaffCode(String staffCode, Pageable pageable);

    /**
     * find all Assignments by asssetId
     *
     * @param assetId request
     * @return List<Assignment>
     */
    List<Assignment> findByAssetId(String assetId);
    @Query("SELECT a.assignmentId.assignmentId from ReturnRequest as a where a.state not like 'REJECT' ")
    List<Integer> listAllAssignmentId();


    @Query("SELECT a from Assignment as a where a.assetId.assetId = ?1 ")
    List<Assignment> getAssByAssetCode(String id);
}





