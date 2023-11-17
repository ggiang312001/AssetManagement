package com.nt.rookies.assets.controllers;

import com.nt.rookies.assets.dtos.Response;
import com.nt.rookies.assets.dtos.UserDto;
import com.nt.rookies.assets.dtos.UserListRequestDto;
import com.nt.rookies.assets.dtos.UserRequestDto;
import com.nt.rookies.assets.entities.Location;
import com.nt.rookies.assets.services.AdminService;
import com.nt.rookies.assets.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/admin")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    private AdminService adminService;

    public UserController(UserService service, AdminService adminService) {
        this.userService = Objects.requireNonNull(service);
        this.adminService = Objects.requireNonNull(adminService);
    }

    /**
     * The public function is used to call paging list users for every page
     */
    @GetMapping("/users")
    public ResponseEntity<Response> listUser(
            @Valid UserListRequestDto userListRequestDto,
            Principal principal
    ) {
        String username = principal.getName();
        UserDto userDto = userService.getByUsername(username);
        Location location = userDto.getLocationId();
        Integer locationId = location.getLocationId();
        return new ResponseEntity<>(userService.listUser(locationId, userListRequestDto), HttpStatus.OK);
    }

    @GetMapping("/users/{staffCode}")
    public ResponseEntity<UserDto> getByStaffCode(@PathVariable String staffCode) {
        return new ResponseEntity<>(userService.getByStaffCode(staffCode), HttpStatus.OK);
    }


    /**
     * Create New User
     *
     * @param userRequestDto firstName, lastName, birthDate, gender, createdAt and role
     * @param principal      Principal of Spring Security which has user details (username, roles)
     * @return userDto user information
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserRequestDto userRequestDto, Principal principal) {
        String adminUsername = principal.getName();
        return new ResponseEntity<UserDto>(userService.createUser(userRequestDto, adminUsername), HttpStatus.CREATED);
    }


    /**
     * Edit User
     *
     * @param userRequestDto firstName, lastName, birthDate, gender, createdAt and role
     * @param staffCode      staff code of updating user
     * @param principal      Principal of Spring Security which has user details (username, roles)
     * @return userDto user information
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/users/{staffCode}")
    public ResponseEntity<UserDto> editUser(@RequestBody @Valid UserRequestDto userRequestDto,
                                            @PathVariable String staffCode, Principal principal) {
        String adminUsername = principal.getName();
        return new ResponseEntity<UserDto>(userService.editUser(userRequestDto, staffCode, adminUsername), HttpStatus.OK);
    }

    /**
     *
     * @param staffCode Staff code of user to disable
     * @param principal  Principal of Spring Security which has user details (username, roles)
     * @return userDto user information
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/users/{staffCode}/disable")
    public ResponseEntity<UserDto> disableUser(
            @PathVariable String staffCode, Principal principal) {
        String adminUsername = principal.getName();
        return new ResponseEntity<UserDto>(userService.disableUser(staffCode, adminUsername), HttpStatus.OK);
    }

}
