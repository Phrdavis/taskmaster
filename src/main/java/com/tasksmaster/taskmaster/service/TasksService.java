package com.tasksmaster.taskmaster.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tasksmaster.taskmaster.dto.TasksDto;
import com.tasksmaster.taskmaster.model.Tasks;
import com.tasksmaster.taskmaster.model.User;
import com.tasksmaster.taskmaster.repository.TasksRepository;
import com.tasksmaster.taskmaster.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class TasksService {

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public TasksDto cadastrar(TasksDto dto) {
        if (tasksRepository.existsByTitle(dto.getTitle())) {
            throw new RuntimeException("Tarefa já cadastrada!");
        }

        if (userRepository.findById(dto.getOwnerId()).isEmpty()) {
            throw new RuntimeException("Responsável ID " + dto.getOwnerId() + " não encontrado!");
        }

        Tasks tasks = new Tasks();
        tasks.setTitle(dto.getTitle());
        tasks.setDescription(dto.getDescription());
        tasks.setOwner(userRepository.findById(dto.getOwnerId()).get());

        return new TasksDto(tasksRepository.save(tasks));

    }

    @Transactional
    public List<TasksDto> cadastrarMultiplos(List<TasksDto> dtos) {
        List<TasksDto> savedTasks = new ArrayList<>();

        for (TasksDto dto : dtos) {
            savedTasks.add(cadastrar(dto));
        }

        return savedTasks;
    }

    public Tasks buscarPorId(Long id) {
        return tasksRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }

    public Page<TasksDto> buscarTodos(Pageable paginacao) {
        return tasksRepository.findAll(paginacao).stream()
            .map(TasksDto::new)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> new PageImpl<>(list)
            ));
    }

    public Page<TasksDto> buscarMinhasTarefas(String email, Pageable paginacao) {

        if(email == null || email.isEmpty() || email.equals("anonymousUser")) {
            throw new RuntimeException("Email do usuário não pode ser nulo ou vazio");
        }

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return tasksRepository.findMyTasks(user.getId(), "", paginacao).stream()
            .map(TasksDto::new)
            .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> new PageImpl<>(list)
            ));
    }

    public TasksDto atualizar(Long id, TasksDto newTask) {
        Tasks existingTask = buscarPorId(id);

        if(newTask.getTitle() != null && !newTask.getTitle().isEmpty()) {
            existingTask.setTitle(newTask.getTitle());
        }

        if(newTask.getDescription() != null && !newTask.getDescription().isEmpty()) {
            existingTask.setDescription(newTask.getDescription());
        }

        if(newTask.getOwnerId() != null) {
            User owner = userRepository.findById(newTask.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Responsável ID " + newTask.getOwnerId() + " não encontrado!")); 
            existingTask.setOwner(owner);
        }
        Tasks tasksSave = tasksRepository.save(existingTask);
        
        return new TasksDto(tasksSave);
    }

    public void deletar(Long id){
        tasksRepository.findById(id)
            .ifPresent(existingTask -> {
                tasksRepository.delete(existingTask);
            });
    }
    
}
