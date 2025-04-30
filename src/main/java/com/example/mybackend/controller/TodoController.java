package com.example.mybackend.controller;

import com.example.mybackend.model.Todo;
import com.example.mybackend.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Todo getByIdTodos(@PathVariable String id){ 
        return todoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Todo not found"));
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable String id, @RequestBody Todo updatedTodo) {
        if (todoRepository.existsById(id)) {
            updatedTodo.setId(id);
            return todoRepository.save(updatedTodo);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        todoRepository.deleteById(id);
    }

    @GetMapping("/search")
    public List<Todo> searchTodos(@RequestParam String query) {
        return todoRepository.findByTitleContainingIgnoreCase(query);
    }

    @GetMapping("/filter")
    public List<Todo> filterTodos(
        @RequestParam(required = false) Boolean completed,
        @RequestParam(required = false) Todo.Priority priority
    ) {
        return todoRepository.findByCompletedAndPriority(completed, priority);
    }

    @PutMapping("/batch/complete")
    public void completeMultiple(@RequestBody List<String> ids) {
        todoRepository.findAllById(ids).forEach(todo -> {
            todo.setCompleted(true);
            todoRepository.save(todo);
        });
    }

    @GetMapping("/stats")
    public Map<String, Object> getStatistics() {
        List<Todo> todos = todoRepository.findAll();
        return Map.of(
            "total", todos.size(),
            "completed", todos.stream().filter(Todo::isCompleted).count(),
            "pending", todos.stream().filter(t -> !t.isCompleted()).count()
        );
    }
} 