package com.nt.rookies.assets.mappers;

import com.nt.rookies.assets.dtos.ReturnRequestDto;
import com.nt.rookies.assets.entities.ReturnRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReturnRequestMapper {

    public static ReturnRequestDto toDto(ReturnRequest entity) {
        ReturnRequestDto returnRequestDto = new ReturnRequestDto();
        returnRequestDto.setRequestId(entity.getRequestId());
        returnRequestDto.setRequestNote(entity.getRequestNote());
        returnRequestDto.setAssignmentId(entity.getAssignmentId());
        returnRequestDto.setCreatedAt(entity.getCreatedAt());
        returnRequestDto.setCreatedBy(UserMapper.toDto(entity.getCreatedBy()));

        if (Objects.nonNull(entity.getAcceptedBy())){
            returnRequestDto.setAcceptedBy(UserMapper.toDto(entity.getAcceptedBy()));
        }

        returnRequestDto.setReturnedDate(entity.getReturnedDate());
        returnRequestDto.setState(entity.getState());
        return returnRequestDto;
    }

    public static ReturnRequest toEntity(ReturnRequestDto returnRequestDto) {
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setRequestId(returnRequestDto.getRequestId());
        returnRequest.setAssignmentId(returnRequestDto.getAssignmentId());
        returnRequest.setRequestNote(returnRequestDto.getRequestNote());
        returnRequest.setCreatedAt(returnRequestDto.getCreatedAt());
        returnRequest.setCreatedBy(UserMapper.toEntity(returnRequestDto.getCreatedBy()));

        if(Objects.nonNull(returnRequestDto.getAcceptedBy())){
            returnRequest.setAcceptedBy(UserMapper.toEntity(returnRequestDto.getAcceptedBy()));
        }

        returnRequest.setReturnedDate(returnRequestDto.getReturnedDate());
        returnRequest.setState(returnRequestDto.getState());
        return returnRequest;
    }


    public static List<ReturnRequestDto> toDtoList(List<ReturnRequest> entities) {
        return entities.stream().map(ReturnRequestMapper::toDto).collect(Collectors.toList());
    }

    public static List<ReturnRequestDto> toDtoList(Iterable<ReturnRequest> entities) {
        List<ReturnRequestDto> returnRequestDto = new LinkedList<>();
        entities.forEach(e -> returnRequestDto.add(toDto(e)));
        return returnRequestDto;
    }

}
