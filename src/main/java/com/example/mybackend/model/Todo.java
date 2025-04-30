package com.example.mybackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
public class Todo {
    // Properties
    @Id
    private String id;        // UUID string
    private String title;     // Text Content
    private boolean completed;  // Status(done or not)
    private Priority priority; // Add this field
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;

    public enum Priority {
        low, medium, high
    }

    public Todo() {
        this.id = UUID.randomUUID().toString();  
        this.createdAt = LocalDateTime.now();
    }

    public Todo(String title, boolean completed) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.completed = completed;
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