package com.mohamedsalah.taskmanager.mapper;

import com.mohamedsalah.taskmanager.dto.request.CreateTaskRequest;
import com.mohamedsalah.taskmanager.dto.response.TaskResponse;
import com.mohamedsalah.taskmanager.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Task entity and DTO transformations.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponse toResponse(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toEntity(CreateTaskRequest request);
}
