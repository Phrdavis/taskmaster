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

import com.tasksmaster.taskmaster.dto.TasksDto;
import com.tasksmaster.taskmaster.service.TasksService;

@RestController
@RequestMapping("/api/v1/tasks")
public class TasksController {

    @Autowired
    private TasksService tasksService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody TasksDto task) {
        return ResponseEntity.ok(tasksService.cadastrar(task));
    }

    @PostMapping("/multiplos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarMultiplos(@RequestBody List<TasksDto> tasks) {
        return ResponseEntity.ok(tasksService.cadastrarMultiplos(tasks));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tasksService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<TasksDto>> buscarTodos(
        @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(tasksService.buscarTodos(pageable));
    }

    @GetMapping("/myTasks")
    public ResponseEntity<Page<TasksDto>> buscarMinhasTarefas(
        @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(tasksService.buscarMinhasTarefas(email, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody TasksDto task) {
        return ResponseEntity.ok(tasksService.atualizar(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        tasksService.deletar(id);
        return ResponseEntity.noContent().build();
    }  
    
}
