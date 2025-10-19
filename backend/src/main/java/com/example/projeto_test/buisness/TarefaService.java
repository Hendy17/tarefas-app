package com.example.projeto_test.buisness;

import com.example.projeto_test.dto.TarefaDTO;
import com.example.projeto_test.dto.TarefaResponseDTO;
import com.example.projeto_test.exception.TarefaNotFoundException;
import com.example.projeto_test.infrastructure.entitys.Tarefa;
import com.example.projeto_test.infrastructure.entitys.repository.TarefaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TarefaService {
    
    private final TarefaRepository tarefaRepository;
    
    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public TarefaResponseDTO createTask(TarefaDTO tarefaDTO) {
        log.info("Creating task: {}", tarefaDTO.getTitulo());
        
        Tarefa tarefa = tarefaDTO.toEntity();
        
        if (tarefa.getStatus() == null) {
            tarefa.setStatus(Tarefa.StatusTarefa.PENDENTE);
            log.debug("Default PENDING status set for task: {}", tarefa.getTitulo());
        }
        
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        log.info("Task created successfully - ID: {}", tarefaSalva.getId());
        
        return TarefaResponseDTO.fromEntity(tarefaSalva);
    }
    
    
    @Transactional(readOnly = true)
    public List<TarefaResponseDTO> getAllTasks() {
        log.info("Listing all tasks");
        List<Tarefa> tarefas = tarefaRepository.findAllByOrderByDataCriacaoDesc();
        log.debug("Found {} tasks", tarefas.size());
        
        return tarefas.stream()
                .map(TarefaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TarefaResponseDTO getTaskById(Long id) {
        log.info("Searching for task with ID: {}", id);
        
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found with ID: {}", id);
                    return new TarefaNotFoundException(id);
                });
        
        log.debug("Task found: {}", tarefa.getTitulo());
        return TarefaResponseDTO.fromEntity(tarefa);
    }
    
    public TarefaResponseDTO updateTask(Long id, TarefaDTO tarefaDTO) {
        log.info("Updating task with ID: {}", id);
        
        Tarefa tarefaExistente = tarefaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempt to update non-existent task - ID: {}", id);
                    return new TarefaNotFoundException(id);
                });
        

        if (tarefaDTO.getTitulo() != null && !tarefaDTO.getTitulo().trim().isEmpty()) {
            tarefaExistente.setTitulo(tarefaDTO.getTitulo().trim());
            log.debug("Title updated to: {}", tarefaDTO.getTitulo());
        }
        

        tarefaExistente.setDescricao(tarefaDTO.getDescricao() != null ? 
                tarefaDTO.getDescricao().trim() : null);
        

        if (tarefaDTO.getStatus() != null) {
            tarefaExistente.setStatus(tarefaDTO.getStatus());
            log.debug("Status updated to: {}", tarefaDTO.getStatus());
        }
        
        Tarefa tarefaSalva = tarefaRepository.save(tarefaExistente);
        log.info("Task updated successfully - ID: {}", tarefaSalva.getId());
        
        return TarefaResponseDTO.fromEntity(tarefaSalva);
    }
    
    public void deleteTask(Long id) {
        log.info("Deleting task with ID: {}", id);
        
        if (!tarefaRepository.existsById(id)) {
            log.warn("Attempt to delete non-existent task - ID: {}", id);
            throw new TarefaNotFoundException(id);
        }
        
        tarefaRepository.deleteById(id);
        log.info("Task deleted successfully - ID: {}", id);
    }
    
    @Transactional(readOnly = true)
    public List<TarefaResponseDTO> getTasksByStatus(Tarefa.StatusTarefa status) {
        log.info("Searching for tasks with status: {}", status);
        
        List<Tarefa> tarefas = tarefaRepository.findByStatus(status);
        log.debug("Found {} tasks with status {}", tarefas.size(), status);
        
        return tarefas.stream()
                .map(TarefaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<TarefaResponseDTO> getTasksByTitle(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            log.warn("Search by empty/null title ignored");
            return List.of();
        }
        
        String tituloLimpo = titulo.trim();
        log.info("Searching for tasks containing: {}", tituloLimpo);
        
        List<Tarefa> tarefas = tarefaRepository.findByTituloContaining(tituloLimpo);
        log.debug("Found {} tasks with title containing '{}'", tarefas.size(), tituloLimpo);
        
        return tarefas.stream()
                .map(TarefaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}