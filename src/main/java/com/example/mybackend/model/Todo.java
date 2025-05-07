package com.example.mybackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;
import java.time.LocalDateTime;

/**
 * Entity representing a Todo item in the system.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull
    private boolean completed;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private LocalDateTime dueDate;

    @NotNull
    private LocalDateTime createdAt;

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public Todo(String title, boolean completed) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.completed = completed;
        this.createdAt = LocalDateTime.now();
        this.priority = Priority.MEDIUM;
    }

    public String getId() { return id; } 
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 