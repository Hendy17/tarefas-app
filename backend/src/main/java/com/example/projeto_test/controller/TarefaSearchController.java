package com.example.projeto_test.controller;

import com.example.projeto_test.dto.TarefaResponseDTO;
import com.example.projeto_test.buisness.TarefaService;
import com.example.projeto_test.exception.BusinessRuleException;
import com.example.projeto_test.infrastructure.entitys.Tarefa;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:3000")
public class TarefaSearchController {

    private final TarefaService tarefaService;

    public TarefaSearchController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TarefaResponseDTO>> searchByStatus(@PathVariable String status) {
        log.info("Searching for tasks with status: {}", status);
        
        try {
            Tarefa.StatusTarefa statusEnum = Tarefa.StatusTarefa.valueOf(status.toUpperCase());
            List<TarefaResponseDTO> tarefas = tarefaService.getTasksByStatus(statusEnum);
            return ResponseEntity.ok(tarefas);
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleException(
                "STATUS_INVALID", 
                status, 
                String.format("Status '%s' is invalid. Accepted values: PENDENTE, CONCLUIDA, CANCELADA", status)
            );
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<TarefaResponseDTO>> searchByTitle(@RequestParam String titulo) {
        log.info("Searching for tasks with title containing: {}", titulo);
        
        if (titulo.trim().length() < 2) {
            throw new BusinessRuleException(
                "SEARCH_TOO_SHORT",
                titulo,
                "Search term must have at least 2 characters"
            );
        }
        
        List<TarefaResponseDTO> tarefas = tarefaService.getTasksByTitle(titulo);
        return ResponseEntity.ok(tarefas);
    }

   
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TarefaResponseDTO> markAsCompleted(@PathVariable Long id) {
        log.info("Marking task {} as completed", id);
        
        TarefaResponseDTO tarefa = tarefaService.getTaskById(id);
        
        if (tarefa.getStatus() == Tarefa.StatusTarefa.CONCLUIDA) {
            throw new BusinessRuleException(
                "ALREADY_COMPLETED",
                tarefa.getStatus().name(),
                String.format("Task '%s' is already completed", tarefa.getTitulo())
            );
        }
        
        if (tarefa.getStatus() == Tarefa.StatusTarefa.CANCELADA) {
            throw new BusinessRuleException(
                "CANNOT_COMPLETE_CANCELLED",
                tarefa.getStatus().name(),
                String.format("Cannot complete task '%s' because it was cancelled", tarefa.getTitulo())
            );
        }
        
        com.example.projeto_test.dto.TarefaDTO dto = com.example.projeto_test.dto.TarefaDTO.builder()
                .titulo(tarefa.getTitulo())
                .descricao(tarefa.getDescricao())
                .status(Tarefa.StatusTarefa.CONCLUIDA)
                .build();
        
        TarefaResponseDTO tarefaAtualizada = tarefaService.updateTask(id, dto);
        return ResponseEntity.ok(tarefaAtualizada);
    }

   
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<TarefaResponseDTO> cancelTask(@PathVariable Long id) {
        log.info("Cancelling task {}", id);
        
        TarefaResponseDTO tarefa = tarefaService.getTaskById(id);
        
        if (tarefa.getStatus() == Tarefa.StatusTarefa.CANCELADA) {
            throw new BusinessRuleException(
                "ALREADY_CANCELLED",
                tarefa.getStatus().name(),
                String.format("Task '%s' is already cancelled", tarefa.getTitulo())
            );
        }
        
        if (tarefa.getStatus() == Tarefa.StatusTarefa.CONCLUIDA) {
            throw new BusinessRuleException(
                "CANNOT_CANCEL_COMPLETED",
                tarefa.getStatus().name(),
                String.format("Cannot cancel task '%s' because it is already completed", tarefa.getTitulo())
            );
        }
        
        com.example.projeto_test.dto.TarefaDTO dto = com.example.projeto_test.dto.TarefaDTO.builder()
                .titulo(tarefa.getTitulo())
                .descricao(tarefa.getDescricao())
                .status(Tarefa.StatusTarefa.CANCELADA)
                .build();
        
        TarefaResponseDTO tarefaAtualizada = tarefaService.updateTask(id, dto);
        return ResponseEntity.ok(tarefaAtualizada);
    }
}