package com.mohamedsalah.taskmanager.mapper;

import com.mohamedsalah.taskmanager.dto.response.UserResponse;
import com.mohamedsalah.taskmanager.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting User entities to UserResponse DTO representations.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole().getName().name())")
    UserResponse toResponse(User user);
}
