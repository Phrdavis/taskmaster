package com.tasksmaster.taskmaster.config;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tasksmaster.taskmaster.dto.ErrorMessage;

@RestControllerAdvice
public class RestExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        
        if (ex.getMessage().toLowerCase().contains("não encontrado")) {
            status = HttpStatus.NOT_FOUND;
        }
        
        ErrorMessage error = new ErrorMessage(
                status.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(error);
    }

}
