package com.example.projeto_test.controller;

import com.example.projeto_test.dto.TarefaDTO;
import com.example.projeto_test.dto.TarefaResponseDTO;
import com.example.projeto_test.buisness.TarefaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:3000")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<TarefaResponseDTO> createTask(@Valid @RequestBody TarefaDTO tarefaDTO) {
        log.info("Creating new task: {}", tarefaDTO.getTitulo());
        TarefaResponseDTO novaTarefa = tarefaService.createTask(tarefaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
    }

    @GetMapping
    public ResponseEntity<List<TarefaResponseDTO>> getAllTasks() {
        log.info("Listing all tasks");
        List<TarefaResponseDTO> tarefas = tarefaService.getAllTasks();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> getTaskById(@PathVariable Long id) {
        log.info("Searching for task with ID: {}", id);
        TarefaResponseDTO tarefa = tarefaService.getTaskById(id);
        return ResponseEntity.ok(tarefa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TarefaDTO tarefaDTO) {
        log.info("Updating task with ID: {} - New title: {}", id, tarefaDTO.getTitulo());
        TarefaResponseDTO tarefa = tarefaService.updateTask(id, tarefaDTO);
        return ResponseEntity.ok(tarefa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("Deleting task with ID: {}", id);
        tarefaService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}