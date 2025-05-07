package com.example.mybackend.service;

import com.example.mybackend.dto.TodoRequest;
import com.example.mybackend.dto.TodoResponse;
import com.example.mybackend.model.Todo;
import com.example.mybackend.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Page<TodoResponse> getTodos(Boolean completed, Todo.Priority priority, Pageable pageable) {
        return todoRepository.findByCompletedAndPriority(completed, priority, pageable)
                .map(this::toResponse);
    }

    public Optional<TodoResponse> getTodoById(String id) {
        return todoRepository.findById(id).map(this::toResponse);
    }

    public TodoResponse createTodo(TodoRequest request) {
        Todo todo = fromRequest(request);
        todo.setCreatedAt(LocalDateTime.now());
        return toResponse(todoRepository.save(todo));
    }

    public TodoResponse updateTodo(String id, TodoRequest request) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        todo.setTitle(request.getTitle());
        todo.setCompleted(request.getCompleted());
        todo.setPriority(Todo.Priority.valueOf(request.getPriority().toUpperCase()));
        if (request.getDueDate() != null) {
            todo.setDueDate(LocalDateTime.parse(request.getDueDate()));
        }
        return toResponse(todoRepository.save(todo));
    }

    public void deleteTodo(String id) {
        todoRepository.deleteById(id);
    }

    public List<TodoResponse> searchTodos(String query) {
        return todoRepository.findByTitleContainingIgnoreCase(query)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public void completeMultiple(List<String> ids) {
        todoRepository.findAllById(ids).forEach(todo -> {
            todo.setCompleted(true);
            todoRepository.save(todo);
        });
    }

    public long countTotal() {
        return todoRepository.count();
    }

    public long countCompleted() {
        return todoRepository.findAll().stream().filter(Todo::isCompleted).count();
    }

    public long countPending() {
        return todoRepository.findAll().stream().filter(t -> !t.isCompleted()).count();
    }

    private Todo fromRequest(TodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setCompleted(request.getCompleted());
        todo.setPriority(Todo.Priority.valueOf(request.getPriority().toUpperCase()));
        if (request.getDueDate() != null) {
            todo.setDueDate(LocalDateTime.parse(request.getDueDate()));
        }
        return todo;
    }

    private TodoResponse toResponse(Todo todo) {
        TodoResponse resp = new TodoResponse();
        resp.setId(todo.getId());
        resp.setTitle(todo.getTitle());
        resp.setCompleted(todo.isCompleted());
        resp.setPriority(todo.getPriority().toString().toLowerCase());
        resp.setDueDate(todo.getDueDate() != null ? todo.getDueDate().toString() : null);
        resp.setCreatedAt(todo.getCreatedAt().toString());
        return resp;
    }
} 