package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.*;
import com.nt.rookies.assets.entities.Assignment;
import com.nt.rookies.assets.entities.Gender;
import com.nt.rookies.assets.entities.Role;
import com.nt.rookies.assets.entities.User;
import com.nt.rookies.assets.exceptions.BadRequestException;
import com.nt.rookies.assets.exceptions.NotFoundException;
import com.nt.rookies.assets.mappers.UserMapper;
import com.nt.rookies.assets.repositories.AssignmentRepository;
import com.nt.rookies.assets.repositories.UserRepository;
import com.nt.rookies.assets.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final AssignmentRepository assignmentRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, AssignmentRepository assignmentRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.assignmentRepository = Objects.requireNonNull(assignmentRepository);
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    /**
     * The public function is used to call paging list users for every page
     */
    public Response listUser(Integer locationId, UserListRequestDto userListRequestDto) {

        String searchTerm = userListRequestDto.getSearchTerm().trim();
        Role role = userListRequestDto.getRole();
        String[] sort = userListRequestDto.getSort();
        Integer pageNo = userListRequestDto.getPageNo();
        Integer pageSize = userListRequestDto.getPageSize();

        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        if (sort[0].contains(",")) {
            // will sort more than 2 columns
            for (String sortOrder : sort) {
                // sortOrder="column, direction"
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[column, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(orders));

        Page<User> users;

        String[] searchKey = searchTerm.trim().split(" ");
        String firstName = searchKey[0];
        String lastName = "";
        String staffCode = searchTerm;
        if (searchKey.length > 1) {
            for (int i = 1; i < searchKey.length; i++) {
                lastName += searchKey[i] + " ";
            }
            lastName = lastName.trim();
        }
        //Write Query
        if (role == null)
            users = userRepository.findBySearchTermAndSorting(locationId, firstName, lastName, staffCode, pageable);
        else {
            users = userRepository.findByFilterAndSearchTermAndSorting(locationId, role, firstName, lastName, staffCode, pageable);
        }

        Response response = new Response();
        List<User> listUser = users.getContent();
        List<UserDto> listUserDto = UserMapper.toDtoList(listUser);

        response.setContent(listUserDto);
        response.setPageNo(users.getNumber() + 1);
        response.setPageSize(users.getSize());
        response.setTotalPages(users.getTotalPages());
        response.setTotalElements(users.getTotalElements());
        response.setLast(users.isLast());
        return response;
    }

    public UserDto getByUsername(String username) {

        return UserMapper.toDto(this.userRepository.findByUsername(username).orElseThrow());
    }

    public UserDto getByStaffCode(String code) {

        return UserMapper.toDto(this.userRepository.findByStaffCode(code).orElseThrow(() -> new NotFoundException("User with username: " + code + " Not Found")));
    }

    public UserDto changePassword(ChangePasswordDto changePasswordDto, String username) {

        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username: " + username + " Not Found"));


        String oldPassword = changePasswordDto.getOldPassword();
        String newPassword = changePasswordDto.getNewPassword();

        // Check if old password is incorrect
        if (oldPassword != null && !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        // Check if new password is same as old password
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BadRequestException("New password must be different from the current password");
        }

        // Check if user is changing password for the first time
        if (user.getFirstLogin() == true) {
            user.setFirstLogin(false);
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(username);

        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    /**
     * Create new user
     *
     * @param userRequestDto firstName, lastName, birthDate, gender, createdAt and role
     * @param adminUsername  get admin to set location of admin to new user
     * @return userDto user information
     */
    public UserDto createUser(UserRequestDto userRequestDto, String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new NotFoundException("User with username: " + adminUsername + " Not Found"));
        ;

        String firstName = userRequestDto.getFirstName();
        String lastName = userRequestDto.getLastName();
        String birthDate = userRequestDto.getBirthDate();
        Gender gender = userRequestDto.getGender();
        String createdAt = userRequestDto.getCreatedAt();
        Role role = userRequestDto.getRole();

        if (Utils.containsNumber(firstName) || Utils.containsNumber(lastName)) {
            throw new BadRequestException("First name and Last name must not contain number");
        }

        if (Utils.isUserUnder18(birthDate)) {
            throw new BadRequestException("User is under 18. Please select a different date");
        }

        LocalDate dob = LocalDate.parse(birthDate);
        LocalDate joinedDate = LocalDate.parse(createdAt);

        if (joinedDate.isBefore(dob.plusYears(18)) || joinedDate.isEqual(dob.plusYears(18))) {
            throw new BadRequestException("Joined date is not later than user's 18th birthday. Please select a different date");
        }

        if (joinedDate.isBefore(dob)) {
            throw new BadRequestException("Joined date is not later than Date of Birth. Please select a different date");
        }

        if (joinedDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || joinedDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new BadRequestException("Joined date is Saturday or Sunday. Please select a different date");
        }

        String rawUsername = Utils.generateUsername(firstName, lastName);
        List<User> usersContainUsername = userRepository.findUsersContainUsername(rawUsername);
        String officialUsername;

//      If this username do not exist, save this generated username
//      else, generate new username with number at the end
        if (usersContainUsername.isEmpty()) {
            officialUsername = rawUsername;
        } else {
            String latestUsername = usersContainUsername.get(0).getUsername();
            officialUsername = Utils.generateUsernameForDb(latestUsername);
        }

        String rawPassword = Utils.generatePassword(officialUsername, birthDate);
        String bcryptPassword = passwordEncoder.encode(rawPassword);

        User newUser = new User();
        newUser.setUsername(officialUsername);
        newUser.setPassword(bcryptPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setBirthDate(LocalDate.parse(birthDate));
        newUser.setGender(gender);
        newUser.setCreatedAt(joinedDate.atStartOfDay());
        newUser.setCreatedBy(adminUsername);
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setUpdatedBy(adminUsername);
        newUser.setActive(true);
        newUser.setFirstLogin(true);
        newUser.setRole(role);
        newUser.setLocationId(admin.getLocationId());

        return UserMapper.toDto(userRepository.save(newUser));

    }

    /**
     * Edit user
     * @param userRequestDto firstName, lastName, birthDate, gender, createdAt and role
     * @param staffCode
     * @param adminUsername
     * @return userDto user information
     */

    public UserDto editUser(UserRequestDto userRequestDto, String staffCode, String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new NotFoundException("Admin with username: " + adminUsername + " Not Found"));
        ;

        User user = this.userRepository.findByStaffCode(staffCode)
                .orElseThrow(() -> new NotFoundException("User with staffCode: " + staffCode + " Not Found"));


        String birthDate = userRequestDto.getBirthDate();
        Gender gender = userRequestDto.getGender();
        String createdAt = userRequestDto.getCreatedAt();
        Role role = userRequestDto.getRole();

        if (Utils.isUserUnder18(birthDate)) {
            throw new BadRequestException("User is under 18. Please select a different date");
        }

        LocalDate dob = LocalDate.parse(birthDate);
        LocalDate joinedDate = LocalDate.parse(createdAt);

        if (joinedDate.isBefore(dob.plusYears(18)) || joinedDate.isEqual(dob.plusYears(18))) {
            throw new BadRequestException("Joined date is not later than user's 18th birthday. Please select a different date");
        }

        if (joinedDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || joinedDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            throw new BadRequestException("Joined date is Saturday or Sunday. Please select a different date");
        }

        user.setBirthDate(LocalDate.parse(birthDate));
        user.setGender(gender);
        user.setCreatedAt(joinedDate.atStartOfDay());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(adminUsername);
        user.setRole(role);

        return UserMapper.toDto(userRepository.save(user));
    }

    /**
     * Disable user
     * @param staffCode
     * @param adminUsername
     * @return userDto user information
     */

    public UserDto disableUser(String staffCode, String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new NotFoundException("Admin with username: " + adminUsername + " Not Found"));
        ;

        User user = this.userRepository.findByStaffCode(staffCode)
                .orElseThrow(() -> new NotFoundException("User with staffCode: " + staffCode + " Not Found"));

        Page<Assignment> assignmentsByUser = assignmentRepository.findByAssignee_StaffCode(staffCode, PageRequest.of(0, 1));

        if (assignmentsByUser.getTotalElements() != 0) {
            throw new BadRequestException("User is having valid assignment");
        }
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(adminUsername);

        return UserMapper.toDto(userRepository.save(user));
    }

}
