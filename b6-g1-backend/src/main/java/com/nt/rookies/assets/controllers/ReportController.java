package com.nt.rookies.assets.controllers;

import com.nt.rookies.assets.dtos.*;
import com.nt.rookies.assets.entities.Location;
import com.nt.rookies.assets.services.ReportExcelService;
import com.nt.rookies.assets.services.ReportService;
import com.nt.rookies.assets.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/report")
@CrossOrigin("*")
public class ReportController {

    private ReportService reportService;

    private ReportExcelService reportExcelService;

    private UserService userService;

    public ReportController(ReportService reportService, ReportExcelService reportExcelService, UserService userService) {
        this.userService = userService;
        this.reportService = reportService;
        this.reportExcelService = reportExcelService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public ResponseEntity<ReportResponse> report(@Valid ReportRequestDto reportRequestDto, Principal principal) {
        String username = principal.getName();
        UserDto userDto = userService.getByUsername(username);
        Location location = userDto.getLocationId();
        Integer locationId = location.getLocationId();
        return new ResponseEntity<>(reportService.viewReportAsset(reportRequestDto, locationId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/export")
    public ResponseEntity<Resource> exportReport(Principal principal) {
        String username = principal.getName();
        UserDto userDto = userService.getByUsername(username);
        Location location = userDto.getLocationId();
        Integer locationId = location.getLocationId();

        ReportResourceDto resource = reportExcelService.exportReport(locationId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment; filename="+"AssetReport.xlsx");

        return ResponseEntity.ok()
                .contentType(resource.getMediaType())
                .headers(httpHeaders)
                .body(resource.getResource());
    }
}
