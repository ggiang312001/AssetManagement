package com.nt.rookies.assets.repositories;

import com.nt.rookies.assets.entities.*;
import com.nt.rookies.assets.exceptions.NotFoundException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
public class AssignmentRepositoryTests {
    private AssignmentRepository assignmentRepository;
    private TestEntityManager testEntityManager;
    @Autowired
    public AssignmentRepositoryTests(AssignmentRepository assetRepository, TestEntityManager enbEntityManager){
        this.assignmentRepository = assetRepository;
        this.testEntityManager = enbEntityManager;
    }
    @Test
    @Order(1)
    public void testGetAssignmentByLocation(){
        int locationId = 1;
        Pageable pageable = PageRequest.of(1,3);
        List<Assignment> actual = assignmentRepository.findAssignmentsByLocationId(locationId, pageable).getContent();
        assertNotNull(actual);
    }
    @Test
    @Order(2)
    public void testGetAssignmentByLocationIdandState(){
        int locationId = 1;
        AssignmentState state= AssignmentState.valueOf("ACCEPTED");
        Pageable pageable = PageRequest.of(1,3);
        Page<Assignment> actual = assignmentRepository.findAssignmentByLocationIdAndState(locationId,state,pageable);

        assertNotNull(actual);
    }

    @Test
    @Order(3)
    public void testGetAssignmentByLocationIdandAssignedDate(){
        int locationId = 1;
        LocalDate date = LocalDate.now();
        Pageable pageable = PageRequest.of(1,3);
        Page<Assignment> actual = assignmentRepository.findAssignmentByLocationIdAndAssignDate(locationId,date ,pageable);

        assertNotNull(actual);
    }
    // private static LocalDateTime installedDate = LocalDateTime.of(2022,02,02,02,02);
    // private static LocalDate assignDate = LocalDate.of(2022,02,02);

    // private static LocalDate birthdate = LocalDate.of(2022,02,02);
    // private static LocalDateTime createdAt = LocalDateTime.of(2022,02,02,02,02);
    // private static LocalDateTime updatedAt = LocalDateTime.of(2022,02,02,02,02);
    // private static Role role = Role.ADMIN;
    // private static Location location = Location.builder().locationId(1).city("city").build();
    // @Test
    // @Order(4)
    // public void testSaveAssignment(){
    //     Location location = new Location();
    //     location.setLocationId(1);
    //     location.setCity("Ha Noi");

    //     Category category = new Category();
    //     category.setCategoryId(1);
    //     category.setName("Monitor");

    //     Asset asset = new Asset();
    //     asset.setAssetId("LA001");
    //     asset.setName("laptop Dell HP Probook 450 G4");
    //     asset.setSpecification("RAM 4GB, HDD 500GB, Intel Core i5 gen 5");
    //     asset.setCategory(category);
    //     asset.setInstalledDate(installedDate);
    //     asset.setLocationId(location);
    //     asset.setState(AssetState.AVAILABLE);

    //     User user = new User();
    //     user.setStaffCode("SD0001");
    //     user.setUsername("vangdv");
    //     user.setPassword("$2a$10$ZSlYKBxG/G9TAxc4UK5Irezg6y0QaIMpEkJa1AyxTBs6SBhJ1NBeu");
    //     user.setFirstName("Vang");
    //     user.setLastName("Do Van");
    //     user.setBirthdate(birthdate);
    //     user.setGender("Male");
    //     user.setEmail("vangdo@gmail.com");
    //     user.setCreatedAt(createdAt);
    //     user.setCreatedBy("root");
    //     user.setUpdatedAt(updatedAt);
    //     user.setUpdatedBy("root");
    //     user.setRole(role);
    //     user.setFirstLogin(true);
    //     user.setActive(true);

    //     user.setLocationId(location);


    //     Assignment assignment = new Assignment();
    //     assignment.setAssignmentId(1);
    //     assignment.setAssigner(user);
    //     assignment.setAssignee(user);
    //     assignment.setAssetId(asset);
    //     assignment.setAssignDate(assignDate);
    //     assignment.setAssignNote("abc");
    //     assignment.setState(AssignmentState.ACCEPTED);

    //     Assignment saved = assignmentRepository.save(assignment);
    //     assertTrue(saved.getAssignmentId()==(assignment.getAssignmentId()));

    // }

    @Test
    @Order(4)
    public void testGetAssignmentByAssignmentID(){
        int id = 1;
        Assignment assignment = assignmentRepository.findByAssignmentId(id).orElseThrow(() ->new NotFoundException("Assignment with assignmentId: " + id + " Not Found"));
        assertEquals(1, assignment.getAssignmentId());
    }

    @Test
    @Order(5)
    public void testSaveAssignment(){

        Assignment assignment = new Assignment(1, new User(), new User(), new Asset(), LocalDate.now(), "note", AssignmentState.ACCEPTED, LocalDateTime.now());

        assignment.setAssignmentId(1);
        assignment.setAssignDate(LocalDate.now());
        assignment.setState(AssignmentState.WAITING_FOR_ACCEPTANCE);

        assignmentRepository.save(assignment);
        assertThat(assignment.getAssignmentId()).isEqualTo(1);
    }

    @Test
    @Order(6)
    public void testFindByAssignee_StaffCode(){

        String staffCode = "SD0003";
        Pageable pageable = PageRequest.of(1, 5);
        Page<Assignment> assignments = assignmentRepository.findByAssignee_StaffCode(staffCode, pageable);

        assertNotNull(assignments);

    }


}
