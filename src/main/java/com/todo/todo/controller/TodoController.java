package com.todo.todo.controller;

import com.todo.todo.exception.TodoCollectionException;
import com.todo.todo.model.TodoDTO;
import com.todo.todo.repository.TodoRepository;
import com.todo.todo.service.TodoService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TodoController {

    @Autowired
    private TodoRepository todoRepo;

    @Autowired
    private TodoService todoService;

    @GetMapping("/todos")
    public ResponseEntity<?> getAllTodos() {
        List<TodoDTO> todos = todoRepo.findAll();
        if (todos.size() > 0) {
            return new ResponseEntity<List<TodoDTO>>(todos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Not todos avaiable", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/todos")
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todo) {
        try {
            todoService.createTodo(todo);
            return new ResponseEntity<TodoDTO>(todo, HttpStatus.CREATED);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (TodoCollectionException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable("id") String id) {
        Optional<TodoDTO> todoDTOOptional = todoRepo.findById(id);
        if (todoDTOOptional.isPresent())
            return new ResponseEntity<>(todoDTOOptional.get(), HttpStatus.OK);
        return new ResponseEntity<>("Todo not found with id " + id, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> uptadeTodoById(@PathVariable("id") String id, @RequestBody TodoDTO newTodo) {
        Optional<TodoDTO> todoDTOOptional = todoRepo.findById(id);
        if (todoDTOOptional.isPresent()) {
            TodoDTO todoToSave = todoDTOOptional.get();
            todoToSave.setCompleted(newTodo.getCompleted() != null ? newTodo.getCompleted() : todoToSave.getCompleted());
            todoToSave.setTodo(newTodo.getTodo() != null ? newTodo.getTodo() : todoToSave.getTodo());
            todoToSave.setDescription(newTodo.getDescription() != null ? newTodo.getDescription() : todoToSave.getDescription());
            todoToSave.setUpdatedAt(new Date(System.currentTimeMillis()));
            todoRepo.save(todoToSave);
            return new ResponseEntity<>(todoToSave, HttpStatus.OK);
        }
        return new ResponseEntity<>("Todo not found with id " + id, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteTodoById(@PathVariable("id") String id) {
        try {
            if (!todoRepo.existsById(id)) {
                throw new IllegalArgumentException("Todo with id " + id + " does not exist");
            }
            todoRepo.deleteById(id);
            return new ResponseEntity<>("Successfully deleted with id " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
