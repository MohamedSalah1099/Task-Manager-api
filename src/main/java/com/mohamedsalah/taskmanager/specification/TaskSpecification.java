package com.mohamedsalah.taskmanager.specification;

import com.mohamedsalah.taskmanager.entity.Task;
import com.mohamedsalah.taskmanager.enums.TaskPriority;
import com.mohamedsalah.taskmanager.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

/**
 * Reusable JPA specifications for querying Task entities.
 */
public final class TaskSpecification {

    private TaskSpecification() {
    }

    public static Specification<Task> hasUserId(Long userId) {
        return (root, query, cb) -> userId == null ? null : cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, cb) -> priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasCategory(String category) {
        return (root, query, cb) -> (category == null || category.isBlank()) ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Task> titleContains(String title) {
        return (root, query, cb) -> (title == null || title.isBlank()) ? null : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Task> categoryContains(String category) {
        return (root, query, cb) -> (category == null || category.isBlank()) ? null : cb.like(cb.lower(root.get("category")), "%" + category.toLowerCase() + "%");
    }
}
