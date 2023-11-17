package com.nt.rookies.assets.controllers;

import com.nt.rookies.assets.dtos.ReturnRequestDto;
import com.nt.rookies.assets.dtos.ReturnRequestListDto;
import com.nt.rookies.assets.dtos.ReturnRequestResponse;
import com.nt.rookies.assets.services.ReturnRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class ReturnRequestController {

    private ReturnRequestService returnRequestService;

    public ReturnRequestController(ReturnRequestService returnRequestService) {
        this.returnRequestService = returnRequestService;
    }

    /**
     * Method handles HTTP GET request to get all return requests
     *
     * @param returnRequestListDto list of return requests, paging, sorting and filtering information
     * @return returnRequestResponse list of return requests
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/return-request")
    public ResponseEntity<ReturnRequestResponse> getAll(ReturnRequestListDto returnRequestListDto) {

        return new ResponseEntity<>(returnRequestService.getAll(returnRequestListDto), HttpStatus.OK);
    }

    /**
     * Method handles HTTP POST request to create new return request
     * @param id id of assignment
     * @param principal store authenticated data from security interceptor
     * @return ReturnRequestDto entity with created AssignmentDto object
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/return-request/assignmentId/{id}")
    public ResponseEntity<ReturnRequestDto> createReturnRequestByAdmin(@PathVariable Integer id, Principal principal) {
        String ussername = principal.getName();
        return new ResponseEntity<>(returnRequestService.createReturnRequest(id, ussername), HttpStatus.CREATED);
    }

    /**
     * Method handles HTTP PUT request to accept return request
     *
     * @param id        id of return request
     * @param principal store authenticated data from security interceptor
     * @return ReturnRequestDto entity with accepted
     */

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/return-request/{id}")
    public ResponseEntity<ReturnRequestDto> acceptReturnRequest(@PathVariable Integer id, Principal principal) {
        return new ResponseEntity<>(returnRequestService.acceptReturnRequest(id, principal.getName()), HttpStatus.ACCEPTED);
    }

    /**
     * Method handles HTTP DELETE request to delete return request
     *
     * @param id id of return request
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/admin/return-request/{id}")
    public ResponseEntity<ReturnRequestDto> deleteReturnRequest(@PathVariable Integer id) {
        return new ResponseEntity<>(returnRequestService.deleteReturnRequest(id), HttpStatus.OK);
    }


    /**
     * Method handles HTTP POST request to create new return request
     * @param id id of assignment
     * @param principal store authenticated data from security interceptor
     * @return ReturnRequestDto entity with created AssignmentDto object
     */

    @PreAuthorize("hasAuthority('STAFF')")
    @PostMapping("/return-request/assignmentId/{id}")
    public ResponseEntity<ReturnRequestDto> createReturnRequestByStaff(@PathVariable Integer id, Principal principal) {
        String ussername = principal.getName();
        return new ResponseEntity<>(returnRequestService.createReturnRequest(id, ussername), HttpStatus.CREATED);
    }
}
