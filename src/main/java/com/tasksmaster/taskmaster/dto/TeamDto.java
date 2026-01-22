package com.tasksmaster.taskmaster.dto;

import java.util.Set;

import com.tasksmaster.taskmaster.model.Team;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class TeamDto {

    private Long id;
    private String name;
    private Long coordinatorId;
    private String coordinatorName;
    private Set<UserDto> members;

    public TeamDto(Long id, String name, Long coordinatorId, String coordinatorName, Set<UserDto> members) {
        this.id = id;
        this.name = name;
        this.coordinatorId = coordinatorId;
        this.coordinatorName = coordinatorName;
        this.members = members;
    }

    public TeamDto(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.coordinatorId = team.getCoordinator().getId();
        this.coordinatorName = team.getCoordinator().getName();
        this.members = team.getMembers().stream()
            .map(UserDto::new)
            .collect(java.util.stream.Collectors.toSet());
    }
    
}
