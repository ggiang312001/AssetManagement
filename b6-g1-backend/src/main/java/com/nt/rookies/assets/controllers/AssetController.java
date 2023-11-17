package com.nt.rookies.assets.controllers;

import com.nt.rookies.assets.dtos.*;
import com.nt.rookies.assets.entities.Location;
import com.nt.rookies.assets.exceptions.ApiException;
import com.nt.rookies.assets.services.AdminService;
import com.nt.rookies.assets.services.AssetService;
import com.nt.rookies.assets.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/admin/assets")
@CrossOrigin("*")
public class AssetController {

    private AssetService assetService;

    private final UserService userService;

    private AdminService adminService;

    public AssetController(AssetService assetService, UserService userService, AdminService adminService) {
        this.assetService = Objects.requireNonNull(assetService);
        this.userService = Objects.requireNonNull(userService);
        this.adminService = Objects.requireNonNull(adminService);
    }


//    @GetMapping
//    public ResponseEntity<List<AssetDto>> getAll(){
//        return new ResponseEntity<>(assetService.getAll(), HttpStatus.OK);
//    }

    /**
     * Method handles HTTP GET request to get list asset (search, fillter, paging)
     *
     * @param assetListRequestDto represents the request data to objects with specified attributes
     * @param principal           store authenticated data from security interceptor
     * @return AssetResponse
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public ResponseEntity<AssetResponse> viewBy(@Valid AssetListRequestDto assetListRequestDto, Principal principal) {
        String username = principal.getName();
        UserDto userDto = userService.getByUsername(username);
        Location location = userDto.getLocationId();
        Integer locationId = location.getLocationId();
        return new ResponseEntity<>(assetService.viewAsset(locationId, assetListRequestDto), HttpStatus.OK);
    }

    /**
     * Method handles HTTP GET request to get asset by assetId
     *
     * @param code assetId
     * @return AssetDto
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{code}")
    public ResponseEntity<AssetDto> getByCode(@PathVariable String code) {
        return new ResponseEntity<>(assetService.getByCode(code), HttpStatus.OK);
    }

    /**
     * Method handle HTTP request create Asset
     *
     * @param assetRequestDto represents the request data to objects with specified attributes
     * @param principal       store authenticated data from security interceptor
     * @return Response entity with created asset object
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public ResponseEntity<AssetDto> createAsset(@RequestBody @Valid AssetRequestDto assetRequestDto, Principal principal) {
        String username = principal.getName();
        String assetName = assetRequestDto.getName().trim();
        String assetSpecification = assetRequestDto.getSpecification().trim();
        System.out.println(assetName);
        if (assetName.equals("") || assetSpecification.equals("")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        assetRequestDto.setName(assetName);
        assetRequestDto.setSpecification(assetSpecification);
        return new ResponseEntity<>(adminService.createAsset(assetRequestDto, username), HttpStatus.CREATED);
    }

    /**
     * Method handle HTTP Post request edit Asset
     *
     * @param id       assetId
     * @param assetDto request data to objects with specified attributes
     * @return AssetDto
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AssetDto> editAsset(@PathVariable String id,
                                              @RequestBody @Valid AssetDto assetDto) {

        String assetName = assetDto.getName().trim();
        String assetSpecification = assetDto.getSpecification().trim();
        if (assetName.equals("") || assetSpecification.equals("")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        assetDto.setName(assetName);
        assetDto.setSpecification(assetSpecification);
        return new ResponseEntity<>(assetService.editAsset(id, assetDto), HttpStatus.OK);
    }

    /**
     * Method handle HTTP PATCH request delete Asset
     * @param id assetId
     * @return AssetDto
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<AssetDto> deleteAsset(@PathVariable String id) {
        return new ResponseEntity<>(assetService.deleteAsset(id), HttpStatus.OK);
    }

    /**
     * Method handle HTTP Get request check Asset was appeared in any assignment
     * @param id id of asset
     * @return Integer
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("check-history/{id}")
    public ResponseEntity<Integer> checkHistoryAsset(@PathVariable String id) {
        return new ResponseEntity<>(assetService.checkHistory(id), HttpStatus.OK);
    }

}
