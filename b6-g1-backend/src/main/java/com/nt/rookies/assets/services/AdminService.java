package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.*;
import com.nt.rookies.assets.entities.*;
import com.nt.rookies.assets.exceptions.ApiException;
import com.nt.rookies.assets.exceptions.NotFoundException;
import com.nt.rookies.assets.mappers.AssetMapper;
import com.nt.rookies.assets.mappers.AssignmentMapper;
import com.nt.rookies.assets.mappers.CategoryMapper;
import com.nt.rookies.assets.mappers.LocationMapper;
import com.nt.rookies.assets.repositories.*;
import com.nt.rookies.assets.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

// import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class AdminService {
    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AssignmentRepository assignmentRepository;

    // public AdminService(){}

    public AdminService(AssetRepository assetRepository, LocationRepository locationRepository, CategoryRepository categoryRepository, UserRepository userRepository, AssignmentRepository assignmentRepository) {
        this.assetRepository = Objects.requireNonNull(assetRepository);
        this.locationRepository = Objects.requireNonNull(locationRepository);
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.userRepository = Objects.requireNonNull(userRepository);
        this.assignmentRepository = Objects.requireNonNull(assignmentRepository);
    }

    /**
     * Method call to AssetRepository to create new asset records
     *
     * @param assetRequestDto object represents input data with attributes: assetId, name, specification, installedDate, state
     * @param username        param that pass the username of the caller from HTTP request
     * @return AssetDto object to be added to response entity
     */
    public AssetDto createAsset(AssetRequestDto assetRequestDto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User with username: " + username + " Not Found"));
        Location location = user.getLocationId();
        LocationDto locationDto = LocationMapper.toDto(location);
        Category category = categoryRepository.findByCategoryId(Integer.parseInt(assetRequestDto.getCategory())).orElseThrow(() -> new NotFoundException("Cannot fetch category"));
        CategoryDto categoryDto = CategoryMapper.toDto(category);
        int amount = assetRepository.findAll().size();
        String prefix = categoryDto.getName().substring(0, 2).toUpperCase();
        assetRequestDto.setAssetId(Utils.IdGenerator(prefix, 3, amount + 1));
        LocalDateTime updatedAt = LocalDateTime.now();
        try {
            return (AssetMapper.toDto(assetRepository.save(AssetMapper.toEntity(assetRequestDto, locationDto, categoryDto, updatedAt))));
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Service failed unexpectedly");
        }
        // return createdAssetDto;
    }

    /**
     * Method call to LocationRepository to get all location records
     *
     * @return
     */
    public List<LocationDto> getAllLocations() {
        return LocationMapper.toDtoList(locationRepository.findAll());
    }

    /**
     * Method calls to CategoryRepository to get all category records
     *
     * @return
     */
    public List<CategoryDto> getAllCategories() {
        return CategoryMapper.toDtoList(categoryRepository.findAll());
    }


    /**
     * Method call to repository to create new assignment
     *
     * @param assignmentRequestDto the object that with attributes: assignee, assigner, assetId, state, assignDate, assignNote
     * @return AssignmentDto object to be add to response entity
     */
    public AssignmentDto createAssignment(AssignmentRequestDto assignmentRequestDto) {
        User assigner = userRepository.findByUsername(assignmentRequestDto.getAssigner()).orElseThrow(() -> new NotFoundException("User with username: " + assignmentRequestDto.getAssigner() + " Not Found"));
        User assignee = userRepository.findByStaffCode(assignmentRequestDto.getAssignee()).orElseThrow(() -> new NotFoundException("User with username: " + assignmentRequestDto.getAssignee() + " Not Found"));
        if(assigner.getLocationId().getLocationId() != assignee.getLocationId().getLocationId()) throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot assign to user in different location");
        Asset asset = assetRepository.findByAssetId(assignmentRequestDto.getAssetId()).orElseThrow(() ->new NotFoundException("Asset with assetId: " + assignmentRequestDto.getAssetId() + " Not Found"));
        if (asset.getState().name()!="AVAILABLE") throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot create assignment with already assigned asset: " + asset.getAssetId());
        LocalDateTime currentDate = LocalDateTime.now();
        if (assignmentRequestDto.getAssignDate().isBefore(currentDate.toLocalDate())){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bad request!");
        }
        try {
            AssignmentDto assignmentDto = AssignmentMapper.toDto(assignmentRepository.save(AssignmentMapper.toEntity(assignmentRequestDto, assigner, assignee, asset, currentDate)));
            asset.setState(AssetState.ASSIGNED);
            assetRepository.save(asset);
            return assignmentDto;
        } catch (Exception e){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Service failed unexpectedly");
        }
    }

    public AssignmentDto editAssignment(AssignmentRequestDto assignmentRequestDto, Integer assignmentId) {
        if (assignmentId != assignmentRequestDto.getAssignmentId()) throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid request persistence.");
        User assignee = userRepository.findByStaffCode(assignmentRequestDto.getAssignee()).orElseThrow(() -> new NotFoundException("User with username: " + assignmentRequestDto.getAssignee() + " Not Found"));
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId).orElseThrow(() -> new NotFoundException("Assignment with ID: " + assignmentRequestDto.getAssignmentId() + " Not Found"));
        if (assignment.getState().name()=="ACCEPTED") throw new ApiException(HttpStatus.BAD_REQUEST, "Bad request. Assignment has been accepted.");
        if (assignment.getAssigner().getLocationId().getLocationId() != assignee.getLocationId().getLocationId()) throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot assign to user in different location");
        Asset newAsset = assetRepository.findByAssetId(assignmentRequestDto.getAssetId()).orElseThrow(() -> new NotFoundException("Asset with assetID: " + assignmentRequestDto.getAssetId() + " Not Found"));
        Asset oldAsset = assetRepository.findByAssetId(assignment.getAssetId().getAssetId()).orElseThrow(() -> new NotFoundException("Asset with assetID: " + assignment.getAssetId().getAssetId() + " Not Found"));
        if ((oldAsset.getAssetId() != newAsset.getAssetId()) && newAsset.getState().name()!="AVAILABLE") throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create assignment with already assigned asset: " + newAsset.getAssetId());
        LocalDateTime currentDate = LocalDateTime.now();
        if (assignmentRequestDto.getAssignDate().isBefore(assignment.getAssignDate())){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bad request!");
        }
        assignment.setAssetId(newAsset);
        assignment.setAssignee(assignee);
        assignment.setAssignDate(assignmentRequestDto.getAssignDate());
        assignment.setUpdatedAt(currentDate);
        assignment.setAssignNote(assignmentRequestDto.getAssignNote());
        try {
            AssignmentDto assignmentDto = AssignmentMapper.toDto(assignmentRepository.save(assignment));
            oldAsset.setState(AssetState.AVAILABLE);
            assetRepository.save(oldAsset);
            newAsset.setState(AssetState.ASSIGNED);
            assetRepository.save(newAsset);
            return assignmentDto;
        } catch (Exception e){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Service failed unexpectedly");
        }
    }

    public AssignmentDto deleteAssignment(Integer assignmentId) {
        Assignment assignment = assignmentRepository.findByAssignmentId(assignmentId).orElseThrow(() -> new NotFoundException("Assignment with ID: " + assignmentId + " Not Found"));
        if(!assignment.getState().equals(AssignmentState.WAITING_FOR_ACCEPTANCE)) throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot delete assignment that is accepted or deleted");
        assignment.setState(AssignmentState.DELETED);
        Asset assignedAsset = assetRepository.findByAssetId(assignment.getAssetId().getAssetId()).orElseThrow(() -> new NotFoundException("Asset with assetID: " + assignment.getAssetId().getAssetId() + " Not Found"));
        assignedAsset.setState(AssetState.AVAILABLE);
        assetRepository.save(assignedAsset);
        return AssignmentMapper.toDto(assignmentRepository.save(assignment));
    }

}
