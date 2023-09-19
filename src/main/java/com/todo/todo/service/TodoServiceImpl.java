package com.todo.todo.service;

import com.todo.todo.exception.TodoCollectionException;
import com.todo.todo.model.TodoDTO;
import com.todo.todo.repository.TodoRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepo;

    @Override
    public void createTodo(TodoDTO todo) throws ConstraintViolationException, TodoCollectionException{
        Optional<TodoDTO> todoOptional = todoRepo.findByTodo(todo.getTodo());
        if(todoOptional.isPresent()){
            throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
        }
        todo.setCreatedAt(new Date(System.currentTimeMillis()));
        todoRepo.save(todo);
    }

    @Override
    public List<TodoDTO> getAllTodos() {
        List<TodoDTO> todos = todoRepo.findAll();
        if (!todos.isEmpty()) {
            return todos;
        } else {
           return new ArrayList<TodoDTO>();
        }
    }

    @Override
    public TodoDTO getTodoById(String id) throws TodoCollectionException {
        Optional<TodoDTO> todoDTOOptional = todoRepo.findById(id);
        if (!todoDTOOptional.isPresent()){
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }
        return todoDTOOptional.get();

    }

    @Override
    public TodoDTO updateTodo(String id, TodoDTO todo) throws TodoCollectionException {
        Optional<TodoDTO> todoWithId = todoRepo.findById(id);
        Optional<TodoDTO> todoWithSameName = todoRepo.findByTodo(todo.getTodo());
        TodoDTO todoToUpdate = todoWithId.get();;
        if(!todoWithId.isPresent()){
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }else {
            if(todoWithSameName.isPresent() && !todoWithSameName.get().getId().equals(id)){
                throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
            }
            todoToUpdate.setCompleted(todo.getCompleted());
            todoToUpdate.setTodo(todo.getTodo());
            todoToUpdate.setDescription(todo.getDescription());
            todoToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
            todoRepo.save(todoToUpdate);
        }
        return todoToUpdate;
    }
}
