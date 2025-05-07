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

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public Page<TodoResponse> getTodos(
        @RequestParam(required = false) Boolean completed,
        @RequestParam(required = false) String priority,
        Pageable pageable
    ) {
        return todoService.getTodos(completed, priority != null ? Todo.Priority.valueOf(priority.toUpperCase()) : null, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getByIdTodos(@PathVariable String id) {
        return todoService.getTodoById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TodoResponse createTodo(@Valid @RequestBody TodoRequest request) {
        return todoService.createTodo(request);
    }

    @PutMapping("/{id}")
    public TodoResponse updateTodo(@PathVariable String id, @Valid @RequestBody TodoRequest request) {
        return todoService.updateTodo(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        todoService.deleteTodo(id);
    }

    @GetMapping("/search")
    public List<TodoResponse> searchTodos(@RequestParam String query) {
        return todoService.searchTodos(query);
    }

    @PutMapping("/batch/complete")
    public void completeMultiple(@RequestBody List<String> ids) {
        todoService.completeMultiple(ids);
    }

    @GetMapping("/stats")
    public Map<String, Object> getStatistics() {
        return Map.of(
            "total", todoService.countTotal(),
            "completed", todoService.countCompleted(),
            "pending", todoService.countPending()
        );
    }
}