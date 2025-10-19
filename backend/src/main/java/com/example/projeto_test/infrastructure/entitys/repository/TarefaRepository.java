package com.example.projeto_test.infrastructure.entitys.repository;

import com.example.projeto_test.infrastructure.entitys.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    
    List<Tarefa> findByStatus(Tarefa.StatusTarefa status);
    
    @Query("SELECT t FROM Tarefa t WHERE t.titulo LIKE %:titulo%")
    List<Tarefa> findByTituloContaining(@Param("titulo") String titulo);
    
    List<Tarefa> findAllByOrderByDataCriacaoDesc();
    
    long countByStatus(Tarefa.StatusTarefa status);
}