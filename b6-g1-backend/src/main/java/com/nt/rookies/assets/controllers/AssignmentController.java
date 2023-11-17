package com.nt.rookies.assets.controllers;

import com.nt.rookies.assets.dtos.AssignmentDto;
import com.nt.rookies.assets.dtos.AssignmentRequestDto;
import com.nt.rookies.assets.dtos.AssignmentResponse;
import com.nt.rookies.assets.dtos.UserDto;
import com.nt.rookies.assets.entities.AssignmentState;
import com.nt.rookies.assets.entities.Location;
import com.nt.rookies.assets.services.AdminService;
import com.nt.rookies.assets.services.AssetService;
import com.nt.rookies.assets.services.AssignmentService;
import com.nt.rookies.assets.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;

    private AssetService assetService;
    private UserService userService;
    private AdminService adminService;

    public AssignmentController(AssignmentService assignmentService, UserService userService, AdminService adminService) {
        this.assignmentService = Objects.requireNonNull(assignmentService);
        this.userService = Objects.requireNonNull(userService);
        this.adminService = Objects.requireNonNull(adminService);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/assignments")
    public ResponseEntity<AssignmentResponse> getAll(
            // @RequestParam(value = "locationId", defaultValue = "1" , required = false ) Integer locationId,
            @RequestParam(value = "searchTerm", defaultValue = "", required = false) String searchTerm,
            @RequestParam(value = "sortBy", defaultValue = "assignmentId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @RequestParam(value = "stateFill", defaultValue = "", required = false) AssignmentState stateFill,
            @RequestParam(value = "dateFill", defaultValue = "", required = false) String dateFill,
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            Principal principal

    ) {
        String username = principal.getName();
        UserDto userDto = userService.getByUsername(username);
        Location location = userDto.getLocationId();
        Integer locationId = location.getLocationId();
        return new ResponseEntity<>(assignmentService.viewAssignment(locationId, searchTerm, sortBy, sortDir, stateFill, dateFill, pageNo, pageSize), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/assignments/{id}")
    public ResponseEntity<AssignmentDto> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(assignmentService.getId(id), HttpStatus.OK);
    }

    /**
     * Method handles HTTP GET request to get assignment by assignmentId
     *
     * @param id assignmentId
     * @return AssignmentDto
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/assignments/{id}")
    public ResponseEntity<AssignmentDto> getByStaff(@PathVariable Integer id) {
        return new ResponseEntity<>(assignmentService.getId(id), HttpStatus.OK);
    }

    /**
     * Method handles HTTP GET request to get list assignment by paging
     *
     * @param pageNo    page number
     * @param pageSize  total element of each page
     * @param principal store authenticated data from security interceptor
     * @return AssignmentResponse
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/assignments")
    public ResponseEntity<AssignmentResponse> viewStaffAssignment(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            Principal principal
    ) {
        String username = principal.getName();
        UserDto userDto = userService.getByUsername(username);
        String staffCode = userDto.getStaffCode();

        return new ResponseEntity<>(assignmentService.viewStaffAssignment(staffCode, pageNo, pageSize), HttpStatus.OK);
    }

    /**
     * Method handles HTTP PUT request to accept assignment
     *
     * @param id assignmentId
     * @return AssignmentDto
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/assignments/{id}/accept")
    public ResponseEntity<AssignmentDto> acceptAssignment(@PathVariable Integer id) {
        return new ResponseEntity<>(assignmentService.acceptAssignment(id), HttpStatus.OK);
    }

    /**
     * Method handles HTTP PUT request to decline assignment
     *
     * @param id assignmentId
     * @return AssignmentDto
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/assignments/{id}/decline")
    public ResponseEntity<AssignmentDto> declineAssignment(@PathVariable Integer id) {
        AssignmentDto assignmentDto = assignmentService.getId(id);
        return new ResponseEntity<>(assignmentService.declineAssignment(id, assignmentDto.getAssetId().getAssetId()), HttpStatus.OK);
    }

    /**
     * Method handles HTTP POST request to create new Assignment
     *
     * @param assignmentRequestDto objects represents request body with specified attributes
     * @param principal
     * @return Response entity with created AssignmentDto object
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/assignments/")
    public ResponseEntity<AssignmentDto> createAssignment(@RequestBody @Valid AssignmentRequestDto assignmentRequestDto, Principal principal) {
        String username = principal.getName();
        assignmentRequestDto.setAssigner(username);
        return new ResponseEntity<>(adminService.createAssignment(assignmentRequestDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/admin/assignments/{id}")
    public ResponseEntity<AssignmentDto> editAssignment(@RequestBody @Valid AssignmentRequestDto assignmentRequestDto, @PathVariable Integer id, Principal principal){
        return new ResponseEntity<>(adminService.editAssignment(assignmentRequestDto, id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/admin/assignments/{id}")
    public ResponseEntity<AssignmentDto> deleteAssignment(@PathVariable Integer id, Principal principal){
        return new ResponseEntity<>(adminService.deleteAssignment(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/assignments/request/{id}")
    public ResponseEntity<List<AssignmentDto>> listRequest(@PathVariable String id){
        return new ResponseEntity<>(assignmentService.findAll(id), HttpStatus.OK);
    }

}
