package com.nt.rookies.assets.mappers;

import com.nt.rookies.assets.dtos.AssignmentDto;
import com.nt.rookies.assets.dtos.AssignmentRequestDto;
import com.nt.rookies.assets.entities.Asset;
import com.nt.rookies.assets.entities.Assignment;
import com.nt.rookies.assets.entities.AssignmentState;
import com.nt.rookies.assets.entities.User;

// import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class AssignmentMapper {

    public  static AssignmentDto toDto(Assignment entities){
        AssignmentDto assignment = new AssignmentDto();
        assignment.setAssignmentId(entities.getAssignmentId());
        assignment.setAssigner(UserMapper.toDto(entities.getAssigner()));
        assignment.setAssignee(UserMapper.toDto(entities.getAssignee()));
        assignment.setAssetId(entities.getAssetId());
        assignment.setAssignDate(entities.getAssignDate());
        assignment.setAssignNote(entities.getAssignNote());
        assignment.setState(entities.getState().toString());
        assignment.setUpdatedAt(entities.getUpdatedAt());
        return  assignment;
    }

public  static List<AssignmentDto> toDtoList(List<Assignment> entities){
return entities.stream().map(AssignmentMapper::toDto).collect(Collectors.toList());
}
    public static List<AssignmentDto> toDtoList(Iterable<Assignment> entities){
        List<AssignmentDto> assignmentDtos = new LinkedList<>();
        entities.forEach(e -> assignmentDtos.add(toDto(e)));
        return assignmentDtos;
    }

    /**
     * @param assignmentRequestDto Assignment object to be parsed
     * @param assigner User entity object to be parsed to assigner field
     * @param assignee User entity object to be parsed to assignee field
     * @param asset Asset entity object to be parsed
     * @param updatedAt LocalDateTime value to be parsed
     * @return Assignment entity
     */
    public static Assignment toEntity(AssignmentRequestDto assignmentRequestDto, User assigner, User assignee, Asset asset, LocalDateTime updatedAt){
        Assignment assignment = new Assignment();
        assignment.setAssigner(assigner);
        assignment.setAssignee(assignee);
        assignment.setAssetId(asset);
        assignment.setAssignDate(assignmentRequestDto.getAssignDate());
        assignment.setAssignNote(assignmentRequestDto.getAssignNote());
        assignment.setState(assignmentRequestDto.getState());
        assignment.setUpdatedAt(updatedAt);
        return assignment;
    }

    public static Assignment toEntity(AssignmentDto assignmentDto){
        Assignment assignment = new Assignment();
        assignment.setAssigner(UserMapper.toEntity(assignmentDto.getAssigner()));
        assignment.setAssignee(UserMapper.toEntity(assignmentDto.getAssignee()));
        assignment.setAssetId(assignmentDto.getAssetId());
        assignment.setAssignDate(assignmentDto.getAssignDate());
        assignment.setAssignNote(assignmentDto.getAssignNote());
        assignment.setState(AssignmentState.valueOf(assignmentDto.getState()));
        assignment.setUpdatedAt(assignmentDto.getUpdatedAt());
        return assignment;
    }

}
