package com.tasksmaster.taskmaster.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorMessage {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}