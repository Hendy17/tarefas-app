package com.example.projeto_test.buisness;

import com.example.projeto_test.dto.TarefaDTO;
import com.example.projeto_test.dto.TarefaResponseDTO;
import com.example.projeto_test.exception.TarefaNotFoundException;
import com.example.projeto_test.infrastructure.entitys.Tarefa;
import com.example.projeto_test.infrastructure.entitys.repository.TarefaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TarefaService - Testes Unitários")
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private TarefaService tarefaService;

    private TarefaDTO tarefaDTO;
    private Tarefa tarefa;
    private final Long TAREFA_ID = 1L;

    @BeforeEach
    void setUp() {
        tarefaDTO = TarefaDTO.builder()
                .titulo("Teste Unitário")
                .descricao("Implementar testes com JUnit e Mockito")
                .status(Tarefa.StatusTarefa.PENDENTE)
                .build();

        tarefa = Tarefa.builder()
                .id(TAREFA_ID)
                .titulo("Teste Unitário")
                .descricao("Implementar testes com JUnit e Mockito")
                .status(Tarefa.StatusTarefa.PENDENTE)
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("Criar Tarefa")
    class CriarTarefaTests {

        @Test
        @DisplayName("Deve criar tarefa com sucesso")
        void deveCriarTarefaComSucesso() {
            when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

            TarefaResponseDTO resultado = tarefaService.criarTarefa(tarefaDTO);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(TAREFA_ID);
            assertThat(resultado.getTitulo()).isEqualTo(tarefaDTO.getTitulo());
            assertThat(resultado.getDescricao()).isEqualTo(tarefaDTO.getDescricao());
            assertThat(resultado.getStatus()).isEqualTo(tarefaDTO.getStatus());
            assertThat(resultado.getDataCriacao()).isNotNull();

            verify(tarefaRepository, times(1)).save(any(Tarefa.class));
        }

        @Test
        @DisplayName("Deve criar tarefa sem descrição")
        void deveCriarTarefaSemDescricao() {
            tarefaDTO.setDescricao(null);
            tarefa.setDescricao(null);
            when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

            TarefaResponseDTO resultado = tarefaService.criarTarefa(tarefaDTO);

        
            assertThat(resultado).isNotNull();
            assertThat(resultado.getDescricao()).isNull();
            verify(tarefaRepository, times(1)).save(any(Tarefa.class));
        }
    }

    @Nested
    @DisplayName("Listar Tarefas")
    class ListarTarefasTests {

        @Test
        @DisplayName("Deve retornar lista de tarefas")
        void deveRetornarListaDeTarefas() {
            Tarefa tarefa2 = Tarefa.builder()
                    .id(2L)
                    .titulo("Segunda Tarefa")
                    .status(Tarefa.StatusTarefa.CONCLUIDA)
                    .dataCriacao(LocalDateTime.now())
                    .build();

            List<Tarefa> tarefas = Arrays.asList(tarefa, tarefa2);
            when(tarefaRepository.findAll()).thenReturn(tarefas);

            List<TarefaResponseDTO> resultado = tarefaService.listarTarefas();

            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getId()).isEqualTo(1L);
            assertThat(resultado.get(1).getId()).isEqualTo(2L);
            verify(tarefaRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há tarefas")
        void deveRetornarListaVaziaQuandoNaoHaTarefas() {
            when(tarefaRepository.findAll()).thenReturn(Arrays.asList());

            List<TarefaResponseDTO> resultado = tarefaService.listarTarefas();

            assertThat(resultado).isEmpty();
            verify(tarefaRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Buscar Tarefa por ID")
    class BuscarTarefaPorIdTests {

        @Test
        @DisplayName("Deve buscar tarefa por ID com sucesso")
        void deveBuscarTarefaPorIdComSucesso() {
            when(tarefaRepository.findById(TAREFA_ID)).thenReturn(Optional.of(tarefa));

            TarefaResponseDTO resultado = tarefaService.buscarTarefaPorId(TAREFA_ID);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(TAREFA_ID);
            assertThat(resultado.getTitulo()).isEqualTo(tarefa.getTitulo());
            verify(tarefaRepository, times(1)).findById(TAREFA_ID);
        }

        @Test
        @DisplayName("Deve lançar exceção quando tarefa não for encontrada")
        void deveLancarExcecaoQuandoTarefaNaoForEncontrada() {
            when(tarefaRepository.findById(TAREFA_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> tarefaService.buscarTarefaPorId(TAREFA_ID))
                    .isInstanceOf(TarefaNotFoundException.class)
                    .hasMessage("Tarefa com ID 1 não foi encontrada");

            verify(tarefaRepository, times(1)).findById(TAREFA_ID);
        }
    }

    @Nested
    @DisplayName("Atualizar Tarefa")
    class AtualizarTarefaTests {

        @Test
        @DisplayName("Deve atualizar tarefa com sucesso")
        void deveAtualizarTarefaComSucesso() {
            TarefaDTO tarefaAtualizadaDTO = TarefaDTO.builder()
                    .titulo("Título Atualizado")
                    .descricao("Descrição Atualizada")
                    .status(Tarefa.StatusTarefa.CONCLUIDA)
                    .build();

            Tarefa tarefaAtualizada = Tarefa.builder()
                    .id(TAREFA_ID)
                    .titulo("Título Atualizado")
                    .descricao("Descrição Atualizada")
                    .status(Tarefa.StatusTarefa.CONCLUIDA)
                    .dataCriacao(LocalDateTime.now().minusDays(1))
                    .dataAtualizacao(LocalDateTime.now())
                    .build();

            when(tarefaRepository.findById(TAREFA_ID)).thenReturn(Optional.of(tarefa));
            when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaAtualizada);

            TarefaResponseDTO resultado = tarefaService.atualizarTarefa(TAREFA_ID, tarefaAtualizadaDTO);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getTitulo()).isEqualTo("Título Atualizado");
            assertThat(resultado.getDescricao()).isEqualTo("Descrição Atualizada");
            assertThat(resultado.getStatus()).isEqualTo(Tarefa.StatusTarefa.CONCLUIDA);
            assertThat(resultado.getDataAtualizacao()).isNotNull();

            verify(tarefaRepository, times(1)).findById(TAREFA_ID);
            verify(tarefaRepository, times(1)).save(any(Tarefa.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar atualizar tarefa inexistente")
        void deveLancarExcecaoAoTentarAtualizarTarefaInexistente() {
            when(tarefaRepository.findById(TAREFA_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> tarefaService.atualizarTarefa(TAREFA_ID, tarefaDTO))
                    .isInstanceOf(TarefaNotFoundException.class)
                    .hasMessage("Tarefa com ID 1 não foi encontrada");

            verify(tarefaRepository, times(1)).findById(TAREFA_ID);
            verify(tarefaRepository, never()).save(any(Tarefa.class));
        }
    }

    @Nested
    @DisplayName("Deletar Tarefa")
    class DeletarTarefaTests {

        @Test
        @DisplayName("Deve deletar tarefa com sucesso")
        void deveDeletarTarefaComSucesso() {
            when(tarefaRepository.existsById(TAREFA_ID)).thenReturn(true);
            doNothing().when(tarefaRepository).deleteById(TAREFA_ID);

            assertThatCode(() -> tarefaService.deletarTarefa(TAREFA_ID))
                    .doesNotThrowAnyException();

            verify(tarefaRepository, times(1)).existsById(TAREFA_ID);
            verify(tarefaRepository, times(1)).deleteById(TAREFA_ID);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar tarefa inexistente")
        void deveLancarExcecaoAoTentarDeletarTarefaInexistente() {
            
            when(tarefaRepository.existsById(TAREFA_ID)).thenReturn(false);

            assertThatThrownBy(() -> tarefaService.deletarTarefa(TAREFA_ID))
                    .isInstanceOf(TarefaNotFoundException.class)
                    .hasMessage("Tarefa com ID 1 não foi encontrada");

            verify(tarefaRepository, times(1)).existsById(TAREFA_ID);
            verify(tarefaRepository, never()).deleteById(TAREFA_ID);
        }
    }
}