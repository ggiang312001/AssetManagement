package com.nt.rookies.assets.mappers;

import com.nt.rookies.assets.dtos.UserDto;
import com.nt.rookies.assets.entities.User;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class UserMapper {

    public static UserDto toDto(User entity) {
        UserDto userDto = new UserDto();
        userDto.setStaffCode(entity.getStaffCode());
        userDto.setUsername(entity.getUsername());
        userDto.setFirstName(entity.getFirstName());
        userDto.setLastName(entity.getLastName());
        userDto.setBirthDate(entity.getBirthDate());
        userDto.setGender(entity.getGender());
        userDto.setEmail(entity.getEmail());
        userDto.setCreatedAt(entity.getCreatedAt());
        userDto.setCreatedBy(entity.getCreatedBy());
        userDto.setUpdatedAt(entity.getUpdatedAt());
        userDto.setUpdatedBy(entity.getUpdatedBy());
        userDto.setActive(entity.getActive());
        userDto.setFirstLogin(entity.getFirstLogin());
        userDto.setRole(entity.getRole());
        userDto.setLocationId(entity.getLocationId());

        return userDto;
    }
    public static List<UserDto> toDtoList(List<User> entities){
        return entities.stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public static List<UserDto> toDtoList(Iterable<User> entities) {
        List<UserDto> userDtos = new LinkedList<>();
        entities.forEach(e -> userDtos.add(toDto(e)));
        return userDtos;
    }

    public static User toEntity(UserDto userDto) {
        User entity = new User();
        entity.setStaffCode(userDto.getStaffCode());
        entity.setUsername(userDto.getUsername());
        entity.setFirstName(userDto.getFirstName());
        entity.setLastName(userDto.getLastName());
        entity.setBirthDate(userDto.getBirthDate());
        entity.setGender(userDto.getGender());
        entity.setEmail(userDto.getEmail());
        entity.setCreatedAt(userDto.getCreatedAt());
        entity.setCreatedBy(userDto.getCreatedBy());
        entity.setUpdatedAt(userDto.getUpdatedAt());
        entity.setUpdatedBy(userDto.getUpdatedBy());
        entity.setActive(userDto.getActive());
        entity.setFirstLogin(userDto.getFirstLogin());
        entity.setRole(userDto.getRole());
        entity.setLocationId(userDto.getLocationId());

        return entity;
    }
}
