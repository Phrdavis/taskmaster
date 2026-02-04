package com.tasksmaster.taskmaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tasksmaster.taskmaster.dto.TeamDto;
import com.tasksmaster.taskmaster.service.TeamService;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TeamDto team) {
        return ResponseEntity.ok(teamService.cadastrar(team));
    }

    @PostMapping("/multiplos")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarMultiplos(@RequestBody List<TeamDto> teams) {
        return ResponseEntity.ok(teamService.cadastrarMultiplos(teams));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<TeamDto>> buscarTodos(
        @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(teamService.buscarTodos(pageable));
    }

    @GetMapping("/myTeams")
    public ResponseEntity<Page<TeamDto>> buscarMinhasEquipes(
        @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(teamService.buscarMinhasEquipes(email, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody TeamDto team) {
        return ResponseEntity.ok(teamService.atualizar(id, team));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        teamService.deletar(id);
        return ResponseEntity.noContent().build();
    }   
    
}
