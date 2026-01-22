package com.tasksmaster.taskmaster.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tasksmaster.taskmaster.dto.TeamDto;
import com.tasksmaster.taskmaster.dto.UserDto;
import com.tasksmaster.taskmaster.model.Team;
import com.tasksmaster.taskmaster.model.User;
import com.tasksmaster.taskmaster.repository.TeamRepository;
import com.tasksmaster.taskmaster.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public TeamDto cadastrar(TeamDto dto) {
        if (teamRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Time já cadastrado!");
        }

        if (userRepository.findById(dto.getCoordinatorId()).isEmpty()) {
            throw new RuntimeException("Coordenador ID " + dto.getCoordinatorId() + " não encontrado!");
        }

        Team team = new Team();
        team.setName(dto.getName());
        team.setCoordinator(userRepository.findById(dto.getCoordinatorId()).get());

        Set<UserDto> members = new HashSet<UserDto>();

        for (UserDto userDto : dto.getMembers()) {
            if (!userRepository.findById(userDto.getId()).isEmpty()) {
                members.add(userDto);
            }
        }

        team.setMembers(members.stream()
            .map(userDto -> userRepository.findById(userDto.getId()).get())
            .collect(Collectors.toSet())  
        );

        return new TeamDto(teamRepository.save(team));


    }

    @Transactional
    public List<TeamDto> cadastrarMultiplos(List<TeamDto> dtos) {
        List<TeamDto> savedTeams = new ArrayList<>();

        for (TeamDto dto : dtos) {
            savedTeams.add(cadastrar(dto));
        }

        return savedTeams;
    }

    public Team buscarPorId(Long id) {
        return teamRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Time não encontrado"));
    }

    public Page<TeamDto> buscarTodos(Pageable paginacao) {
        return teamRepository.findAll(paginacao).stream()
            .map(TeamDto::new)
            .collect(java.util.stream.Collectors.collectingAndThen(
                java.util.stream.Collectors.toList(),
                list -> new org.springframework.data.domain.PageImpl<>(list)
            ));
    }

    public Page<TeamDto> buscarMinhasEquipes(String email, Pageable paginacao) {

        if(email == null || email.isEmpty() || email.equals("anonymousUser")) {
            throw new RuntimeException("Email do usuário não pode ser nulo ou vazio");
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return teamRepository.findMyTeams(user.getId(), "", paginacao).stream()
            .map(TeamDto::new)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> new PageImpl<>(list)
            ));
    }

    public TeamDto atualizar(Long id, TeamDto newTeam) {
        Team existingTeam = buscarPorId(id);

        if(newTeam.getName() != null && !newTeam.getName().isEmpty()) {
            existingTeam.setName(newTeam.getName());
        }
        if(newTeam.getCoordinatorId() != null) {
            User coordinator = userRepository.findById(newTeam.getCoordinatorId())
                .orElseThrow(() -> new RuntimeException("Coordenador ID " + newTeam.getCoordinatorId() + " não encontrado!")); 
            existingTeam.setCoordinator(coordinator);
        }
        if(newTeam.getMembers() != null) {
            Set<User> members = new HashSet<User>();
            for (UserDto userDto : newTeam.getMembers()) {
                User member = userRepository.findById(userDto.getId())
                    .orElseThrow(() -> new RuntimeException("Membro ID " + userDto.getId() + " não encontrado!"));
                members.add(member);
            }
            existingTeam.setMembers(members);
        }
        
        Team teamSave = teamRepository.save(existingTeam);
        
        return new TeamDto(teamSave);
    }

    public void deletar(Long id){
        teamRepository.findById(id)
            .ifPresent(existingTeam -> {
                teamRepository.delete(existingTeam);
            });
    }
    
}
