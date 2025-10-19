package com.example.projeto_test.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Error")
                .message("Invalid data provided")
                .details(errors)
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("Constraint violation: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Constraint Violation")
                .message("Data does not meet validation criteria")
                .details(errors)
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Internal server error: ", ex);
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .details(Map.of("exception", ex.getClass().getSimpleName()))
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    

    @ExceptionHandler(TarefaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTarefaNotFoundException(TarefaNotFoundException ex) {
        log.warn("Tarefa não encontrada: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Tarefa Não Encontrada")
                .message(ex.getMessage())
                .details(Map.of("id", ex.getId().toString()))
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
   
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("JSON malformado recebido: {}", ex.getMessage());
        
        String message = "JSON inválido ou malformado";
        Map<String, String> details = new HashMap<>();
        
        if (ex.getCause() instanceof InvalidFormatException invalidFormatEx) {
            if (invalidFormatEx.getTargetType().isEnum()) {
                message = "Valor inválido para campo enum";
                details.put("campo", invalidFormatEx.getPath().get(0).getFieldName());
                details.put("valorRecebido", invalidFormatEx.getValue().toString());
                details.put("valoresValidos", java.util.Arrays.toString(invalidFormatEx.getTargetType().getEnumConstants()));
            }
        } else if (ex.getCause() instanceof JsonMappingException jsonEx) {
            details.put("campo", jsonEx.getPath().isEmpty() ? "root" : jsonEx.getPath().get(0).getFieldName());
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("JSON Inválido")
                .message(message)
                .details(details)
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
   
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("Tipo de argumento inválido: {} para parâmetro {}", ex.getValue(), ex.getName());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Tipo de Parâmetro Inválido")
                .message(String.format("O parâmetro '%s' deve ser do tipo %s", ex.getName(), ex.getRequiredType().getSimpleName()))
                .details(Map.of(
                    "parametro", ex.getName(),
                    "valorRecebido", ex.getValue().toString(),
                    "tipoEsperado", ex.getRequiredType().getSimpleName()
                ))
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("Método HTTP não suportado: {} para URL {}", ex.getMethod(), request.getRequestURI());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .error("Método Não Permitido")
                .message(String.format("Método HTTP '%s' não é suportado para esta URL", ex.getMethod()))
                .details(Map.of(
                    "metodoRecebido", ex.getMethod(),
                    "metodosSuportados", String.join(", ", ex.getSupportedMethods()),
                    "url", request.getRequestURI()
                ))
                .build();
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }
    
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.warn("Tipo de mídia não suportado: {}", ex.getContentType());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .error("Tipo de Mídia Não Suportado")
                .message("O tipo de conteúdo da requisição não é suportado")
                .details(Map.of(
                    "tipoRecebido", ex.getContentType() != null ? ex.getContentType().toString() : "null",
                    "tiposSuportados", ex.getSupportedMediaTypes().toString()
                ))
                .build();
        
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }
   
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.warn("Endpoint não encontrado: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Endpoint Não Encontrado")
                .message(String.format("O endpoint '%s %s' não existe", ex.getHttpMethod(), ex.getRequestURL()))
                .details(Map.of(
                    "metodo", ex.getHttpMethod(),
                    "url", ex.getRequestURL()
                ))
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.warn("Parâmetro obrigatório ausente: {}", ex.getParameterName());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Parâmetro Obrigatório Ausente")
                .message(String.format("O parâmetro obrigatório '%s' não foi fornecido", ex.getParameterName()))
                .details(Map.of(
                    "parametro", ex.getParameterName(),
                    "tipo", ex.getParameterType()
                ))
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
   
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariableException(MissingPathVariableException ex) {
        log.warn("Variável de path ausente: {}", ex.getVariableName());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Variável de Path Ausente")
                .message(String.format("A variável de path '%s' é obrigatória", ex.getVariableName()))
                .details(Map.of("variavel", ex.getVariableName()))
                .build();
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
   
    @ExceptionHandler({DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(Exception ex) {
        log.error("Violação de integridade de dados: {}", ex.getMessage());
        
        String message = "Violação de regra de integridade dos dados";
        Map<String, String> details = new HashMap<>();
        
        if (ex.getMessage().contains("duplicate") || ex.getMessage().contains("unique")) {
            message = "Já existe um registro com esses dados únicos";
            details.put("tipo", "DUPLICIDADE");
        } else if (ex.getMessage().contains("foreign key") || ex.getMessage().contains("constraint")) {
            message = "Violação de relacionamento entre dados";
            details.put("tipo", "CONSTRAINT");
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflito de Dados")
                .message(message)
                .details(details)
                .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
   
    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorResponse> handleDataConflictException(DataConflictException ex) {
        log.warn("Conflito de dados: {}", ex.getMessage());
        
        Map<String, String> details = new HashMap<>();
        if (ex.getField() != null) {
            details.put("campo", ex.getField());
            details.put("valor", ex.getValue().toString());
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflito de Dados")
                .message(ex.getMessage())
                .details(details)
                .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleException(BusinessRuleException ex) {
        log.warn("Violação de regra de negócio: {}", ex.getMessage());
        
        Map<String, String> details = new HashMap<>();
        if (ex.getRule() != null) {
            details.put("regra", ex.getRule());
        }
        if (ex.getCurrentState() != null) {
            details.put("estadoAtual", ex.getCurrentState());
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error("Regra de Negócio Violada")
                .message(ex.getMessage())
                .details(details)
                .build();
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }
}