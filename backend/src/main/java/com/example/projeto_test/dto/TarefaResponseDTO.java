package com.example.projeto_test.dto;

import com.example.projeto_test.infrastructure.entitys.Tarefa;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarefaResponseDTO {
    
    private Long id;
    private String titulo;
    private String descricao;
    private Tarefa.StatusTarefa status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
   
    public static TarefaResponseDTO fromEntity(Tarefa tarefa) {
        return TarefaResponseDTO.builder()
                .id(tarefa.getId())
                .titulo(tarefa.getTitulo())
                .descricao(tarefa.getDescricao())
                .status(tarefa.getStatus())
                .dataCriacao(tarefa.getDataCriacao())
                .dataAtualizacao(tarefa.getDataAtualizacao())
                .build();
    }
}