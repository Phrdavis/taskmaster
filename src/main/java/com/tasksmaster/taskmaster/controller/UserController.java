package com.tasksmaster.taskmaster.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tasksmaster.taskmaster.dto.UserDto;
import com.tasksmaster.taskmaster.service.UserService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.cadastrar(user));
    }

    @PostMapping("/multiplos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> criarMultiplos(@RequestBody List<UserDto> users) {
        return ResponseEntity.ok(userService.cadastrarMultiplos(users));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> buscarTodos(
        @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(userService.buscarTodos(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody UserDto user) {
        return ResponseEntity.ok(userService.atualizar(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        userService.deletar(id);
        return ResponseEntity.noContent().build();
    }   
    
}
