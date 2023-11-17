package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.AssetDto;
import com.nt.rookies.assets.dtos.AssignmentDto;
import com.nt.rookies.assets.dtos.AssignmentResponse;
import com.nt.rookies.assets.entities.Assignment;
import com.nt.rookies.assets.entities.AssignmentState;
import com.nt.rookies.assets.exceptions.NotFoundException;
import com.nt.rookies.assets.mappers.AssignmentMapper;
import com.nt.rookies.assets.repositories.AssignmentRepository;
import com.nt.rookies.assets.repositories.ReturnRequestRepository;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    private ReturnRequestRepository returnRequestRepository;
    @Autowired
    private AssetService assetService;


    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = Objects.requireNonNull(assignmentRepository);
    }

    public List<AssignmentDto> getAll() {
        return AssignmentMapper.toDtoList(this.assignmentRepository.findAll());
    }

    public AssignmentResponse viewAssignment(Integer locaionId, String searchTerm, String sortBy, String sortDir, AssignmentState stateFill, String dateFill, Integer pageNo, Integer pageSize) {
        AssignmentResponse assignmentResponse = new AssignmentResponse();
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Assignment> assignments;
        if (searchTerm.trim().isEmpty() && stateFill == null && dateFill.isEmpty()) {
            assignments = assignmentRepository.findAssignmentsByLocationId(locaionId, pageable);
        } else if (!searchTerm.trim().isEmpty() && stateFill == null && dateFill.isEmpty()) {
            assignments = assignmentRepository.searchAssignment(
                    locaionId, searchTerm, searchTerm, searchTerm, pageable);

        } else if (searchTerm.trim().isEmpty() && stateFill != null && dateFill.isEmpty()) {
            assignments = assignmentRepository.findAssignmentByLocationIdAndState(locaionId, stateFill, pageable);
        } else if (searchTerm.trim().isEmpty() && stateFill != null && !dateFill.isEmpty()) {
            Map<String, LocalDate> period = getPeriod(dateFill);
            LocalDate from = period.get("from");
            assignments = assignmentRepository.findAssignmentByLocationIdAndStateAndAssignDate
                    (locaionId, stateFill, from, pageable);
        } else if (!searchTerm.trim().isEmpty() && stateFill != null && !dateFill.isEmpty()) {
            Map<String, LocalDate> period = getPeriod(dateFill);
            LocalDate from = period.get("from");
            assignments = assignmentRepository.findAssignmentByAll
                    (locaionId, stateFill, from, searchTerm, searchTerm, searchTerm, pageable);
        } else if (!searchTerm.trim().isEmpty() && stateFill != null && dateFill.isEmpty()) {
            assignments = assignmentRepository.findAssignmentByStateAndSearchTerm(locaionId, stateFill, searchTerm, searchTerm, searchTerm, pageable);
        } else if (!searchTerm.trim().isEmpty() && stateFill == null && !dateFill.isEmpty()) {
            Map<String, LocalDate> period = getPeriod(dateFill);
            LocalDate from = period.get("from");
            assignments = assignmentRepository.findAssignmentByDateAndSearchTerm(locaionId, from, searchTerm, searchTerm, searchTerm, pageable);
        } else {

            Map<String, LocalDate> period = getPeriod(dateFill);
            LocalDate from = period.get("from");

            assignments = assignmentRepository.findAssignmentByLocationIdAndAssignDate(locaionId, from, pageable);
        }

        List<Integer> listId = assignmentRepository.listAllAssignmentId();
        List<Assignment> listAssignments = assignments.getContent();
        List<AssignmentDto> content = AssignmentMapper.toDtoList(listAssignments);
        assignmentResponse.setContent(content);
        assignmentResponse.setPageNo(assignments.getNumber() + 1);
        assignmentResponse.setPageSize(assignments.getSize());
        assignmentResponse.setTotalPages(assignments.getTotalPages());
        assignmentResponse.setTotalElements(assignments.getTotalElements());
        assignmentResponse.setLast(assignments.isLast());
        assignmentResponse.setAssignmentIdReq(listId);
        return assignmentResponse;
    }
//

    public AssignmentDto getId(Integer id) {
        return AssignmentMapper.toDto(assignmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Post Id : " + id + " Not Found")));
    }


    private Map<String, LocalDate> getPeriod(String fromDate) {
        Map<String, LocalDate> period = new HashMap<>();
        LocalDate from = LocalDate.parse(fromDate);
        period.put("from", from);
        return period;
    }

    public AssignmentResponse viewStaffAssignment(String staffCode, Integer pageNo, Integer pageSize) {

        AssignmentResponse assignmentResponse = new AssignmentResponse();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Assignment> assignments;
        assignments = assignmentRepository.findByAssignee_StaffCode(staffCode, pageable);
        List<Integer> listId = assignmentRepository.listAllAssignmentId();
        List<Assignment> listAssignments = assignments.getContent();
        List<AssignmentDto> content = AssignmentMapper.toDtoList(listAssignments);
        assignmentResponse.setContent(content);
        assignmentResponse.setPageNo(assignments.getNumber() + 1);
        assignmentResponse.setPageSize(assignments.getSize());
        assignmentResponse.setTotalPages(assignments.getTotalPages());
        assignmentResponse.setTotalElements(assignments.getTotalElements());
        assignmentResponse.setLast(assignments.isLast());
        assignmentResponse.setAssignmentIdReq(listId);

        return assignmentResponse;

    }

    public AssignmentDto acceptAssignment(Integer assignmentId) {

        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId).orElseThrow(() ->new NotFoundException("Assignment with assignmentId: " + assignmentId + " Not Found"));
        if(assignment.getState().equals(AssignmentState.WAITING_FOR_ACCEPTANCE)){
            assignment.setState(AssignmentState.ACCEPTED);
        }
        return AssignmentMapper.toDto(assignmentRepository.save(assignment));

    }

    public AssignmentDto declineAssignment(Integer id, String assetId){
        Assignment assignment = assignmentRepository.findByAssignmentId(id).orElseThrow(() ->new NotFoundException("Assignment with assignmentId: " + id + " Not Found"));
        if(assignment.getState().equals(AssignmentState.WAITING_FOR_ACCEPTANCE)){
            assignment.setState(AssignmentState.DECLINED);
            AssetDto assetDto = assetService.declineAssignment(assetId);
        }
        return AssignmentMapper.toDto(assignmentRepository.save(assignment));

    }

    public List<AssignmentDto> findAll(String id){
        List<AssignmentDto> list = AssignmentMapper.toDtoList(assignmentRepository.getAssByAssetCode(id)) ;
        return list;
    }

}
