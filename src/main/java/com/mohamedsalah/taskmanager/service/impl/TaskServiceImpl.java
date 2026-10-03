package com.mohamedsalah.taskmanager.service.impl;

import com.mohamedsalah.taskmanager.dto.request.CreateTaskRequest;
import com.mohamedsalah.taskmanager.dto.request.UpdateTaskRequest;
import com.mohamedsalah.taskmanager.dto.response.TaskResponse;
import com.mohamedsalah.taskmanager.exception.TaskNotFoundException;
import com.mohamedsalah.taskmanager.exception.UserNotFoundException;
import com.mohamedsalah.taskmanager.mapper.TaskMapper;
import com.mohamedsalah.taskmanager.entity.Task;
import com.mohamedsalah.taskmanager.entity.User;
import com.mohamedsalah.taskmanager.enums.TaskPriority;
import com.mohamedsalah.taskmanager.enums.TaskStatus;
import com.mohamedsalah.taskmanager.repository.TaskRepository;
import com.mohamedsalah.taskmanager.repository.UserRepository;
import com.mohamedsalah.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of TaskService for CRUD and search operations.
 */
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public TaskResponse createTask(CreateTaskRequest request, String email) {
        User user = findUserByEmail(email);
        Task task = taskMapper.toEntity(request);
        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id, String email) {
        User user = findUserByEmail(email);
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getUserTasks(String email, Pageable pageable) {
        User user = findUserByEmail(email);
        return taskRepository.findByUserId(user.getId(), pageable)
                .map(taskMapper::toResponse);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, UpdateTaskRequest request, String email) {
        User user = findUserByEmail(email);
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        if (request.title() != null) task.setTitle(request.title());
        if (request.description() != null) task.setDescription(request.description());
        if (request.status() != null) task.setStatus(request.status());
        if (request.priority() != null) task.setPriority(request.priority());
        if (request.dueDate() != null) task.setDueDate(request.dueDate());
        if (request.category() != null) task.setCategory(request.category());

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id, String email) {
        User user = findUserByEmail(email);
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> searchTasks(String email, String title, String category, Pageable pageable) {
        User user = findUserByEmail(email);
        Specification<Task> spec = Specification.where(hasUserId(user.getId()));
        if (title != null && !title.isEmpty()) spec = spec.and(titleContains(title));
        if (category != null && !category.isEmpty()) spec = spec.and(categoryContains(category));
        return taskRepository.findAll(spec, pageable).map(taskMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> filterTasks(String email, TaskStatus status, TaskPriority priority, String category, Pageable pageable) {
        User user = findUserByEmail(email);
        Specification<Task> spec = Specification.where(hasUserId(user.getId()));
        if (status != null) spec = spec.and(hasStatus(status));
        if (priority != null) spec = spec.and(hasPriority(priority));
        if (category != null && !category.isEmpty()) spec = spec.and(categoryContains(category));
        return taskRepository.findAll(spec, pageable).map(taskMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(taskMapper::toResponse);
    }

    @Override
    @Transactional
    public void adminDeleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        taskRepository.delete(task);
    }

    private Specification<Task> hasUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    private Specification<Task> titleContains(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    private Specification<Task> categoryContains(String category) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("category")), "%" + category.toLowerCase() + "%");
    }

    private Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    private Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, cb) -> cb.equal(root.get("priority"), priority);
    }
}
