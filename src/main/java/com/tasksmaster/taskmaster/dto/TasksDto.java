package com.tasksmaster.taskmaster.dto;

import com.tasksmaster.taskmaster.model.Tasks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class TasksDto {

    private Long id;
    private String title;
    private String description;
    private Long ownerId;
    private String ownerName;
    private String ownerEmail;
    private String ownerRole;

    public TasksDto(Long id, String title, String description, Long ownerId, String ownerName, String ownerEmail, String ownerRole) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.ownerRole = ownerRole;
    }

    public TasksDto(Tasks task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.ownerId = task.getOwner().getId();
        this.ownerName = task.getOwner().getName();
        this.ownerEmail = task.getOwner().getEmail();
        this.ownerRole = task.getOwner().getRole().name();
    }
    
}
