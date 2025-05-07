package com.example.mybackend.controller;

import com.example.mybackend.dto.TodoRequest;
import com.example.mybackend.dto.TodoResponse;
import com.example.mybackend.model.Todo;
import com.example.mybackend.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing Todo operations.
 */
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * GET /api/todos : Get all todos with optional filters
     */
    @GetMapping
    public ResponseEntity<Page<TodoResponse>> getTodos(
        @RequestParam(required = false) Boolean completed,
        @RequestParam(required = false) String priority,
        Pageable pageable
    ) {
        return ResponseEntity.ok(todoService.getTodos(
            completed, 
            priority != null ? Todo.Priority.valueOf(priority.toUpperCase()) : null, 
            pageable
        ));
    }

    /**
     * GET /api/todos/{id} : Get a todo by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable String id) {
        return todoService.getTodoById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/todos : Create a new todo
     */
    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody TodoRequest request) {
        return ResponseEntity.ok(todoService.createTodo(request));
    }

    /**
     * PUT /api/todos/{id} : Update an existing todo
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(
        @PathVariable String id, 
        @Valid @RequestBody TodoRequest request
    ) {
        return ResponseEntity.ok(todoService.updateTodo(id, request));
    }

    /**
     * DELETE /api/todos/{id} : Delete a todo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/todos/search : Search todos by title
     */
    @GetMapping("/search")
    public ResponseEntity<List<TodoResponse>> searchTodos(@RequestParam String query) {
        return ResponseEntity.ok(todoService.searchTodos(query));
    }

    /**
     * PUT /api/todos/batch/complete : Mark multiple todos as completed
     */
    @PutMapping("/batch/complete")
    public ResponseEntity<Void> completeMultiple(@RequestBody List<String> ids) {
        todoService.completeMultiple(ids);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/todos/stats : Get todo statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStatistics() {
        return ResponseEntity.ok(todoService.getStatistics());
    }
}