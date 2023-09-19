package com.todo.todo.service;

import com.todo.todo.exception.TodoCollectionException;
import com.todo.todo.model.TodoDTO;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

public interface TodoService {
    public void createTodo(TodoDTO todo)throws ConstraintViolationException, TodoCollectionException;
    public List<TodoDTO> getAllTodos();
    public TodoDTO getTodoById(String id) throws TodoCollectionException;
    public TodoDTO updateTodo(String id, TodoDTO todo) throws TodoCollectionException;

}
