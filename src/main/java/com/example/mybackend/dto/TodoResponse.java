package com.example.mybackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoResponse {
    private String id;
    private String title;
    private boolean completed;
    private String priority;
    private String dueDate;
    private String createdAt;
} 