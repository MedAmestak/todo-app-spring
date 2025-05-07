package com.example.mybackend.service;

import com.example.mybackend.dto.TodoRequest;
import com.example.mybackend.dto.TodoResponse;
import com.example.mybackend.model.Todo;
import com.example.mybackend.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for managing Todo operations.
 */
@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * Retrieves a paginated list of todos with optional filters.
     */
    public Page<TodoResponse> getTodos(Boolean completed, Todo.Priority priority, Pageable pageable) {
        return todoRepository.findByCompletedAndPriority(completed, priority, pageable)
                .map(this::toResponse);
    }

    /**
     * Retrieves a todo by its ID.
     */
    public Optional<TodoResponse> getTodoById(String id) {
        return todoRepository.findById(id).map(this::toResponse);
    }

    /**
     * Creates a new todo.
     */
    @Transactional
    public TodoResponse createTodo(TodoRequest request) {
        Todo todo = fromRequest(request);
        todo.setCreatedAt(LocalDateTime.now());
        return toResponse(todoRepository.save(todo));
    }

    /**
     * Updates an existing todo.
     */
    @Transactional
    public TodoResponse updateTodo(String id, TodoRequest request) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        updateTodoFromRequest(todo, request);
        return toResponse(todoRepository.save(todo));
    }

    /**
     * Deletes a todo by its ID.
     */
    @Transactional
    public void deleteTodo(String id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }

    /**
     * Searches todos by title.
     */
    public List<TodoResponse> searchTodos(String query) {
        return todoRepository.findByTitleContainingIgnoreCase(query)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Marks multiple todos as completed in a single transaction.
     */
    @Transactional
    public void completeMultiple(List<String> ids) {
        List<Todo> todos = todoRepository.findAllById(ids);
        todos.forEach(todo -> todo.setCompleted(true));
        todoRepository.saveAll(todos);
    }

    /**
     * Returns statistics about todos.
     */
    public Map<String, Long> getStatistics() {
        return Map.of(
            "total", todoRepository.count(),
            "completed", todoRepository.countByCompleted(true),
            "pending", todoRepository.countByCompleted(false)
        );
    }

    private void updateTodoFromRequest(Todo todo, TodoRequest request) {
        todo.setTitle(request.getTitle());
        todo.setCompleted(request.getCompleted());
        todo.setPriority(Todo.Priority.valueOf(request.getPriority().toUpperCase()));
        if (request.getDueDate() != null) {
            todo.setDueDate(LocalDateTime.parse(request.getDueDate()));
        }
    }

    private Todo fromRequest(TodoRequest request) {
        Todo todo = new Todo();
        updateTodoFromRequest(todo, request);
        return todo;
    }

    private TodoResponse toResponse(Todo todo) {
        return TodoResponse.builder()
            .id(todo.getId())
            .title(todo.getTitle())
            .completed(todo.isCompleted())
            .priority(todo.getPriority().toString().toLowerCase())
            .dueDate(todo.getDueDate() != null ? todo.getDueDate().toString() : null)
            .createdAt(todo.getCreatedAt().toString())
            .build();
    }
} 