package com.example.projeto_test.dto;

import com.example.projeto_test.infrastructure.entitys.Tarefa;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarefaDTO {
    
    @NotBlank(message = "Title is required and cannot be empty")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Pattern(regexp = "^[\\p{L}\\p{N}\\p{P}\\s]+$", message = "Title contains invalid characters")
    private String titulo;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String descricao;
    
    @NotNull(message = "Status is required")
    private Tarefa.StatusTarefa status;
    
    public Tarefa toEntity() {
        return Tarefa.builder()
                .titulo(this.titulo != null ? this.titulo.trim() : null)
                .descricao(this.descricao != null ? this.descricao.trim() : null)
                .status(this.status)
                .build();
    }
    
    public static TarefaDTO fromEntity(Tarefa tarefa) {
        return TarefaDTO.builder()
                .titulo(tarefa.getTitulo())
                .descricao(tarefa.getDescricao())
                .status(tarefa.getStatus())
                .build();
    }
}