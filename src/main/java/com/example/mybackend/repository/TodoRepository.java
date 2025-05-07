package com.example.mybackend.repository;

import com.example.mybackend.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, String> {
    List<Todo> findByTitleContainingIgnoreCase(String query);
    
    @Query("SELECT t FROM Todo t WHERE " +
           "(:completed IS NULL OR t.completed = :completed) AND " +
           "(:priority IS NULL OR t.priority = :priority)")
    Page<Todo> findByCompletedAndPriority(Boolean completed, Todo.Priority priority, Pageable pageable);
} 