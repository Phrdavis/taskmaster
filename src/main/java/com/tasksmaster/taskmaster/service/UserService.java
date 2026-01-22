package com.tasksmaster.taskmaster.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tasksmaster.taskmaster.dto.UserDto;
import com.tasksmaster.taskmaster.enums.Roles;
import com.tasksmaster.taskmaster.model.User;
import com.tasksmaster.taskmaster.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public UserDto cadastrar(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(Enum.valueOf(Roles.class, dto.getRole()));
        
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user = userRepository.save(user);
        return new UserDto(user);
    }

    @Transactional
    public List<UserDto> cadastrarMultiplos(List<UserDto> dtos) {
        List<UserDto> savedUsers = new ArrayList<>();

        for (UserDto dto : dtos) {
            savedUsers.add(cadastrar(dto));
        }

        return savedUsers;
    }

    public UserDto buscarPorId(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário ID " + id + " não encontrado!"));

        return new UserDto(user);
    }

    public Page<UserDto> buscarTodos(Pageable paginacao) {
        return userRepository.findAll(paginacao).stream()
            .map(UserDto::new)
            .collect(java.util.stream.Collectors.collectingAndThen(
                java.util.stream.Collectors.toList(),
                list -> new org.springframework.data.domain.PageImpl<>(list)
            ));
    }

    public UserDto atualizar(Long id, UserDto newUser) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário ID " + id + " não encontrado!"));

        existingUser.setName(newUser.getName());

        if(userRepository.existsByEmail(newUser.getEmail()) && !existingUser.getEmail().equals(newUser.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }

        if(userRepository.existsByEmail(newUser.getEmail()) && !existingUser.getEmail().equals(newUser.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }   

        if(newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
            existingUser.setEmail(newUser.getEmail());
        }
        
        if(newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }

        if(newUser.getRole() != null && !newUser.getRole().isEmpty()) {
            existingUser.setRole(Enum.valueOf(Roles.class, newUser.getRole()));
        }
        User userSave = userRepository.save(existingUser);
        
        return new UserDto(userSave);
    }

    public void deletar(Long id){
        userRepository.findById(id)
            .ifPresent(existingUser -> {
                userRepository.delete(existingUser);
            });
    }
    
}
