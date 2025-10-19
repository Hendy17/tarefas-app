package com.example.projeto_test.exception;

import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {
    
    private final String rule;
    private final String currentState;
    
    public BusinessRuleException(String rule, String currentState, String message) {
        super(message);
        this.rule = rule;
        this.currentState = currentState;
    }
    
    public BusinessRuleException(String message) {
        super(message);
        this.rule = null;
        this.currentState = null;
    }
}