package com.tasksmaster.taskmaster.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class LoginDto {
    private String email;
    private String password;
}