package com.example.projeto_test.exception;

import lombok.Getter;

@Getter
public class TarefaNotFoundException extends RuntimeException {
    
    private final Long id;
    
    public TarefaNotFoundException(Long id) {
        super("Task with ID " + id + " was not found");
        this.id = id;
    }
}