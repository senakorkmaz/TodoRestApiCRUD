package com.todo.todo.service;

import com.todo.todo.exception.TodoCollectionException;
import com.todo.todo.model.TodoDTO;
import jakarta.validation.ConstraintViolationException;

public interface TodoService {
    public void createTodo(TodoDTO todo)throws ConstraintViolationException, TodoCollectionException;

}
