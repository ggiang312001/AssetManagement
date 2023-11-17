package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.AssignmentDto;
import com.nt.rookies.assets.dtos.ReturnRequestDto;
import com.nt.rookies.assets.dtos.ReturnRequestListDto;
import com.nt.rookies.assets.dtos.ReturnRequestResponse;
import com.nt.rookies.assets.entities.*;
import com.nt.rookies.assets.exceptions.ApiException;
import com.nt.rookies.assets.exceptions.NotFoundException;
import com.nt.rookies.assets.mappers.AssignmentMapper;
import com.nt.rookies.assets.mappers.ReturnRequestMapper;
import com.nt.rookies.assets.mappers.UserMapper;
import com.nt.rookies.assets.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReturnRequestService {

    private final ReturnRequestRepository returnRequestRepository;
    private final ReturnRequestCriteriaRepository returnRequestCriteriaRepository;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private AssetRepository assetRepository;

    public ReturnRequestService(ReturnRequestRepository returnRequestRepository, ReturnRequestCriteriaRepository returnRequestCriteriaRepository, UserRepository userRepository, AssignmentRepository assignmentRepository, AssetRepository assetRepository) {
        this.returnRequestRepository = returnRequestRepository;
        this.returnRequestCriteriaRepository = returnRequestCriteriaRepository;
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.assetRepository = assetRepository;
    }


    /**
     * Method call to repository to get all Return requests
     *
     * @param returnRequestListDto list of return requests, paging, sorting and filtering information
     * @return returnRequestResponse list of return requests
     */
    public ReturnRequestResponse getAll(ReturnRequestListDto returnRequestListDto) {
        Page<ReturnRequest> returnRequests = returnRequestCriteriaRepository.findAll(returnRequestListDto);
        ReturnRequestResponse returnRequestResponse = new ReturnRequestResponse();

        List<ReturnRequest> listReturnRequests = returnRequests.getContent();
        List<ReturnRequestDto> content = ReturnRequestMapper.toDtoList(listReturnRequests);

        returnRequestResponse.setContent(content);
        returnRequestResponse.setPageNo(returnRequests.getNumber() + 1);
        returnRequestResponse.setPageSize(returnRequests.getSize());
        returnRequestResponse.setTotalPages(returnRequests.getTotalPages());
        returnRequestResponse.setTotalElements(returnRequests.getTotalElements());
        returnRequestResponse.setLast(returnRequests.isLast());

        return returnRequestResponse;
    }


    /**
     * Method call to repository to create new ReturnRequest
     *
     * @param assignmentId id of assignment user want to return
     * @param username user create return request
     * @return ReturnRequestDto object to be add to response entity
     */
    public ReturnRequestDto createReturnRequest(Integer assignmentId, String username){
        User user = userRepository.findByUsername(username).orElseThrow(() ->new NotFoundException("User with username: " + username + " Not Found"));
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId).orElseThrow(() ->new NotFoundException("Assignment with assignmentId: " + assignmentId + " Not Found"));
        assignmentRepository.save(assignment);
        ReturnRequestDto returnRequest = new ReturnRequestDto();
        returnRequest.setAssignmentId(assignment);
        returnRequest.setCreatedAt(LocalDateTime.now());
        returnRequest.setCreatedBy(UserMapper.toDto(user));
        returnRequest.setState(ReturnRequestState.WAITING_FOR_RETURNING);

        try {
            return ReturnRequestMapper.toDto(returnRequestRepository.save(ReturnRequestMapper.toEntity(returnRequest)));
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Service create return request failed unexpectedly");
        }
    }

    /**
     * Method call to repository to accept ReturnRequest
     *
     * @param returnRequestId id of return request
     * @return ReturnRequestDto object to be accepted
     */
    public ReturnRequestDto acceptReturnRequest(Integer returnRequestId, String acceptedBy) {

        User user = userRepository.findByUsername(acceptedBy).orElseThrow(() -> new NotFoundException("User with username: " + acceptedBy + " Not Found"));

        ReturnRequest returnRequest = returnRequestRepository.findById(returnRequestId).orElseThrow(() -> new NotFoundException("ReturnRequest with returnRequestId: " + returnRequestId + " Not Found"));
        returnRequest.setReturnedDate(LocalDateTime.now());
        returnRequest.setState(ReturnRequestState.COMPLETED);
        returnRequest.setAcceptedBy(user);

        Assignment assignment = assignmentRepository.findByAssignmentId(returnRequest.getAssignmentId().getAssignmentId()).orElseThrow(() -> new NotFoundException("Assignment with assignmentId: " + returnRequest.getAssignmentId().getAssignmentId() + " Not Found"));
        assignment.setState(AssignmentState.DELETED);
        assignmentRepository.save(assignment);

        Asset asset = assetRepository.findByAssetId(assignment.getAssetId().getAssetId()).orElseThrow(() -> new NotFoundException("Asset with assetId: " + assignment.getAssetId().getAssetId() + " Not Found"));
        asset.setState(AssetState.AVAILABLE);
        assetRepository.save(asset);

        try {
            return ReturnRequestMapper.toDto(returnRequestRepository.save(returnRequest));
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Service failed unexpectedly");
        }
    }

    /**
     * Method call to repository to delete ReturnRequest
     *
     * @param returnRequestId id of return request
     */
    public ReturnRequestDto deleteReturnRequest(Integer returnRequestId) {
        ReturnRequest returnRequest = returnRequestRepository.findById(returnRequestId).orElseThrow(() -> new NotFoundException("ReturnRequest with returnRequestId: " + returnRequestId + " Not Found"));
        if (returnRequest.getState().equals(ReturnRequestState.WAITING_FOR_RETURNING)) {
            returnRequest.setState(ReturnRequestState.REJECT);

        }
        try {
            return ReturnRequestMapper.toDto(returnRequestRepository.save(returnRequest));
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Service failed unexpectedly");
        }
    }



}
