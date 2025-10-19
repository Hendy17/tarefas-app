package com.example.projeto_test.exception;

import lombok.Getter;

@Getter
public class DataConflictException extends RuntimeException {
    
    private final String field;
    private final Object value;
    
    public DataConflictException(String field, Object value, String message) {
        super(message);
        this.field = field;
        this.value = value;
    }
    
    public DataConflictException(String message) {
        super(message);
        this.field = null;
        this.value = null;
    }
}