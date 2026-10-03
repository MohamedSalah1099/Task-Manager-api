package com.mohamedsalah.taskmanager.service.impl;

import com.mohamedsalah.taskmanager.dto.request.CreateTaskRequest;
import com.mohamedsalah.taskmanager.dto.response.TaskResponse;
import com.mohamedsalah.taskmanager.entity.Task;
import com.mohamedsalah.taskmanager.entity.User;
import com.mohamedsalah.taskmanager.enums.TaskPriority;
import com.mohamedsalah.taskmanager.enums.TaskStatus;
import com.mohamedsalah.taskmanager.exception.TaskNotFoundException;
import com.mohamedsalah.taskmanager.mapper.TaskMapper;
import com.mohamedsalah.taskmanager.repository.TaskRepository;
import com.mohamedsalah.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for TaskServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("Should create task successfully when valid request is provided")
    void shouldCreateTask() {
        String email = "john@example.com";
        CreateTaskRequest request = new CreateTaskRequest(
                "Sample Task",
                "Description",
                TaskStatus.TODO,
                TaskPriority.HIGH,
                "Work",
                LocalDateTime.now().plusDays(1)
        );

        User user = User.builder()
                .id(1L)
                .email(email)
                .username("john")
                .build();

        Task taskToSave = Task.builder()
                .title("Sample Task")
                .description("Description")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.HIGH)
                .category("Work")
                .build();

        Task savedTask = Task.builder()
                .id(100L)
                .title("Sample Task")
                .description("Description")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.HIGH)
                .category("Work")
                .user(user)
                .build();

        TaskResponse expectedResponse = new TaskResponse(
                100L,
                "Sample Task",
                "Description",
                TaskStatus.TODO,
                TaskPriority.HIGH,
                "Work",
                request.dueDate(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(taskMapper.toEntity(request)).willReturn(taskToSave);
        given(taskRepository.save(taskToSave)).willReturn(savedTask);
        given(taskMapper.toResponse(savedTask)).willReturn(expectedResponse);

        TaskResponse actualResponse = taskService.createTask(request, email);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.title(), actualResponse.title());
        verify(taskRepository).save(taskToSave);
    }

    @Test
    @DisplayName("Should return task when task is owned by authenticated user")
    void shouldReturnTaskWhenOwnedByUser() {
        Long taskId = 100L;
        String email = "john@example.com";

        User user = User.builder()
                .id(1L)
                .email(email)
                .username("john")
                .build();

        Task task = Task.builder()
                .id(taskId)
                .title("Sample Task")
                .user(user)
                .build();

        TaskResponse expectedResponse = new TaskResponse(
                taskId,
                "Sample Task",
                "Description",
                TaskStatus.TODO,
                TaskPriority.HIGH,
                "Work",
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(taskRepository.findByIdAndUserId(taskId, user.getId())).willReturn(Optional.of(task));
        given(taskMapper.toResponse(task)).willReturn(expectedResponse);

        TaskResponse actualResponse = taskService.getTaskById(taskId, email);

        assertNotNull(actualResponse);
        assertEquals(taskId, actualResponse.id());
        verify(taskRepository).findByIdAndUserId(taskId, user.getId());
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when task does not exist or user is not owner")
    void shouldThrowExceptionWhenTaskNotFound() {
        Long taskId = 999L;
        String email = "john@example.com";

        User user = User.builder()
                .id(1L)
                .email(email)
                .username("john")
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(taskRepository.findByIdAndUserId(taskId, user.getId())).willReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId, email));
        verify(taskRepository).findByIdAndUserId(taskId, user.getId());
    }
}
