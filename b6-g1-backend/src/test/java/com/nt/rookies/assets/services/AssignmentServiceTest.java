package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.*;
import com.nt.rookies.assets.entities.*;
import com.nt.rookies.assets.exceptions.NotFoundException;
import com.nt.rookies.assets.mappers.AssetMapper;
import com.nt.rookies.assets.mappers.UserMapper;
import com.nt.rookies.assets.repositories.AssetRepository;
import com.nt.rookies.assets.repositories.AssignmentRepository;
import com.nt.rookies.assets.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AssignmentServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdminService adminService;
    //------------------------------------
    @Mock
    private AssignmentRepository assignmentRepository;

    @InjectMocks
    private AssignmentService assignmentService;

    @Mock
    private AssetService assetService;


    @Mock
    private AssetRepository assetRepository;

//    @Test
//    public void testAssignmentAccepted() {
//        Integer assignmentId = 10;
//        String assetId = "LA001";
//        Asset asset = new Asset();
//        asset.setAssetId(assetId);
//
//        //Assignment assignment = new Assignment(assignmentId, new User(), new User(), asset, LocalDate.now(), "note", AssignmentState.ACCEPTED, LocalDateTime.now());
//        Assignment assignment = new Assignment(assignmentId, new User(), new User(), asset, LocalDate.now(), "note",AssignmentState.ACCEPTED, LocalDateTime.now());
//        Mockito.when(assignmentRepository.findByAssignmentId(10).orElseThrow()).thenReturn(assignment);
//        Mockito.when(assignmentRepository.save(assignment)).thenReturn(assignment);
//        AssignmentDto result = assignmentService.acceptAssignment(assignmentId);
//
//        Mockito.verify(assignmentRepository, Mockito.times(1)).save(assignment);
//        assertTrue(result.getState().equals("ACCEPTED"));
//    }

   // private static AssignmentDto assignmentDto = new AssignmentDto(10, new User(), new User(), new Asset(), LocalDate.now(), "nothing", "State");
    private static AssignmentDto assignmentDto = new AssignmentDto(10, UserMapper.toDto(new User()), UserMapper.toDto(new User()), new Asset(), LocalDate.now(), "nothing", "State", LocalDateTime.now());
    private static String name = "laptop Dell HP Probook 450 G4";
    private static String specification = "RAM 4GB, HDD 500GB, Intel Core i5 gen 5";
    private static CategoryDto category = CategoryDto.builder().categoryId(1).name("sampleName").build();
    private static LocalDateTime installedDate = LocalDateTime.of(2022, 02, 02, 02, 02);
    private static LocationDto location = LocationDto.builder().locationId(1).city("sampleName").build();
    private static AssetState State = AssetState.AVAILABLE;

    @Test
    public void testCreateAssignment() {
        Integer assignmentId = 10;
        String assetId = "LA001";
        // Asset asset = new Asset(assetId, name, specification, new Category(), installedDate, new Location(), AssetState.AVAILABLE);
        //  asset.setAssetId(assetId);

        //Assignment assignment = new Assignment(assignmentId, User.builder().staffCode("SD0001").username("vangdv").build(), User.builder().staffCode("SD0001").username("vangdv").build(), asset, LocalDate.now(), "note", AssignmentState.ACCEPTED);
        LocalDate assignedDate = LocalDate.parse("2000-01-21");
        AssignmentRequestDto assignmentRequestDto = new AssignmentRequestDto(assignmentId ,"assigner", "assignee", assetId, assignedDate, "nothing", AssignmentState.ACCEPTED);

        Mockito.when(adminService.createAssignment(assignmentRequestDto)).thenReturn(assignmentDto);

        AssignmentDto result = adminService.createAssignment(assignmentRequestDto);
        Mockito.verify(adminService, Mockito.times(1)).createAssignment(assignmentRequestDto);

        assertThat(result.getAssignmentId()).isEqualTo(assignmentId);
    }

    private static LocalDate assetInstalledDate = LocalDate.parse("2000-01-21");

    private static AssetRequestDto assetRequestDto = new AssetRequestDto("id", "name", "spe", "cate", assetInstalledDate, 1, AssetState.AVAILABLE);
    private static AssetMapper assetMapper = new AssetMapper();

    @Test
    public void testEditAssignment() {
        Integer assignmentId = 2;
        AssignmentDto dto = new AssignmentDto(2, UserMapper.toDto(new User()), UserMapper.toDto(new User()), new Asset(), LocalDate.now(), "nothing", "WAITING_FOR_ACCEPTANCE", LocalDateTime.now());
        String assetId = "LA001";
        LocalDate assignedDate = LocalDate.now();
        AssignmentRequestDto assignmentRequestDto = new AssignmentRequestDto(assignmentId ,"assigner", "assignee", assetId, assignedDate, "nothing", AssignmentState.WAITING_FOR_ACCEPTANCE);

        Mockito.when(adminService.editAssignment(assignmentRequestDto, assignmentId)).thenReturn(dto);

        AssignmentDto result = adminService.editAssignment(assignmentRequestDto, assignmentId);
        System.out.println(result.toString());
        Mockito.verify(adminService, Mockito.times(1)).editAssignment(assignmentRequestDto, assignmentId);

        assertThat(result.getAssignmentId()).isEqualTo(assignmentId);

    }

    @Test
    public void testDeleteAssignment() {
        Integer assignmentId = 2;
        AssignmentDto dto = new AssignmentDto(2, UserMapper.toDto(new User()), UserMapper.toDto(new User()), new Asset(), LocalDate.now(), "nothing", "State", LocalDateTime.now());

        Mockito.when(adminService.deleteAssignment(assignmentId)).thenReturn(dto);

        AssignmentDto result = adminService.deleteAssignment(assignmentId);
        Mockito.verify(adminService, Mockito.times(1)).deleteAssignment(assignmentId);

        assertThat(result.getAssignmentId()).isEqualTo(assignmentId);
    }

}

