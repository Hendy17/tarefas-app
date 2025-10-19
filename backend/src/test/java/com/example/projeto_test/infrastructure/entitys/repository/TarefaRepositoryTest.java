package com.example.projeto_test.infrastructure.entitys.repository;

import com.example.projeto_test.infrastructure.entitys.Tarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("TarefaRepository - Testes de Integração JPA")
class TarefaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TarefaRepository tarefaRepository;

    private Tarefa tarefa1;
    private Tarefa tarefa2;
    private Tarefa tarefa3;

    @BeforeEach
    void setUp() {
        tarefa1 = Tarefa.builder()
                .titulo("Implementar Testes")
                .descricao("Criar testes unitários e de integração")
                .status(Tarefa.StatusTarefa.PENDENTE)
                .dataCriacao(LocalDateTime.now())
                .build();

        tarefa2 = Tarefa.builder()
                .titulo("Configurar Docker")
                .descricao("Configurar ambiente Docker")
                .status(Tarefa.StatusTarefa.CONCLUIDA)
                .dataCriacao(LocalDateTime.now().minusDays(1))
                .dataAtualizacao(LocalDateTime.now().minusHours(1))
                .build();

        tarefa3 = Tarefa.builder()
                .titulo("Estudar Spring Security")
                .status(Tarefa.StatusTarefa.CANCELADA)
                .dataCriacao(LocalDateTime.now().minusDays(2))
                .build();
    }

    @Nested
    @DisplayName("Operações CRUD Básicas")
    class OperacoesCrudBasicasTests {

        @Test
        @DisplayName("Deve salvar tarefa no banco")
        void deveSalvarTarefaNoBanco() {
            Tarefa tarefaSalva = tarefaRepository.save(tarefa1);

            assertThat(tarefaSalva).isNotNull();
            assertThat(tarefaSalva.getId()).isNotNull();
            assertThat(tarefaSalva.getTitulo()).isEqualTo("Implementar Testes");
            assertThat(tarefaSalva.getDescricao()).isEqualTo("Criar testes unitários e de integração");
            assertThat(tarefaSalva.getStatus()).isEqualTo(Tarefa.StatusTarefa.PENDENTE);
            assertThat(tarefaSalva.getDataCriacao()).isNotNull();
        }

        @Test
        @DisplayName("Deve buscar tarefa por ID")
        void deveBuscarTarefaPorId() {
            Tarefa tarefaSalva = entityManager.persistAndFlush(tarefa1);

            Optional<Tarefa> tarefaEncontrada = tarefaRepository.findById(tarefaSalva.getId());

            assertThat(tarefaEncontrada).isPresent();
            assertThat(tarefaEncontrada.get().getId()).isEqualTo(tarefaSalva.getId());
            assertThat(tarefaEncontrada.get().getTitulo()).isEqualTo("Implementar Testes");
        }

        @Test
        @DisplayName("Deve retornar Optional vazio para ID inexistente")
        void deveRetornarOptionalVazioParaIdInexistente() {
            Optional<Tarefa> tarefaEncontrada = tarefaRepository.findById(999L);

            assertThat(tarefaEncontrada).isEmpty();
        }

        @Test
        @DisplayName("Deve listar todas as tarefas")
        void deveListarTodasAsTarefas() {
            entityManager.persist(tarefa1);
            entityManager.persist(tarefa2);
            entityManager.persist(tarefa3);
            entityManager.flush();

            List<Tarefa> tarefas = tarefaRepository.findAll();

            assertThat(tarefas).hasSize(3);
            assertThat(tarefas)
                    .extracting(Tarefa::getTitulo)
                    .containsExactlyInAnyOrder(
                            "Implementar Testes",
                            "Configurar Docker",
                            "Estudar Spring Security"
                    );
        }

        @Test
        @DisplayName("Deve atualizar tarefa existente")
        void deveAtualizarTarefaExistente() {
            Tarefa tarefaSalva = entityManager.persistAndFlush(tarefa1);
            entityManager.clear();

            
            tarefaSalva.setTitulo("Título Atualizado");
            tarefaSalva.setDescricao("Descrição Atualizada");
            tarefaSalva.setStatus(Tarefa.StatusTarefa.CONCLUIDA);
            tarefaSalva.setDataAtualizacao(LocalDateTime.now());

            Tarefa tarefaAtualizada = tarefaRepository.save(tarefaSalva);

            assertThat(tarefaAtualizada.getTitulo()).isEqualTo("Título Atualizado");
            assertThat(tarefaAtualizada.getDescricao()).isEqualTo("Descrição Atualizada");
            assertThat(tarefaAtualizada.getStatus()).isEqualTo(Tarefa.StatusTarefa.CONCLUIDA);
            assertThat(tarefaAtualizada.getDataAtualizacao()).isNotNull();
        }

        @Test
        @DisplayName("Deve deletar tarefa por ID")
        void deveDeletarTarefaPorId() {
            Tarefa tarefaSalva = entityManager.persistAndFlush(tarefa1);
            Long tarefaId = tarefaSalva.getId();

            tarefaRepository.deleteById(tarefaId);

            Optional<Tarefa> tarefaDeletada = tarefaRepository.findById(tarefaId);
            assertThat(tarefaDeletada).isEmpty();
        }

        @Test
        @DisplayName("Deve verificar se tarefa existe por ID")
        void deveVerificarSeTarefaExistePorId() {
            Tarefa tarefaSalva = entityManager.persistAndFlush(tarefa1);

            boolean existe = tarefaRepository.existsById(tarefaSalva.getId());
            boolean naoExiste = tarefaRepository.existsById(999L);

            assertThat(existe).isTrue();
            assertThat(naoExiste).isFalse();
        }
    }

    @Nested
    @DisplayName("Validação de Constraints")
    class ValidacaoConstraintsTests {

        @Test
        @DisplayName("Deve rejeitar tarefa com título nulo")
        void deveRejeitarTarefaComTituloNulo() {
            Tarefa tarefaInvalida = Tarefa.builder()
                    .titulo(null)
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .dataCriacao(LocalDateTime.now())
                    .build();

            assertThatThrownBy(() -> {
                entityManager.persistAndFlush(tarefaInvalida);
            }).isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Deve rejeitar tarefa com status nulo")
        void deveRejeitarTarefaComStatusNulo() {
            Tarefa tarefaInvalida = Tarefa.builder()
                    .titulo("Título Válido")
                    .status(null)
                    .dataCriacao(LocalDateTime.now())
                    .build();

            assertThatThrownBy(() -> {
                entityManager.persistAndFlush(tarefaInvalida);
            }).isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Deve rejeitar tarefa com data de criação nula")
        void deveRejeitarTarefaComDataCriacaoNula() {
            Tarefa tarefaInvalida = Tarefa.builder()
                    .titulo("Título Válido")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .dataCriacao(null)
                    .build();

            assertThatThrownBy(() -> {
                entityManager.persistAndFlush(tarefaInvalida);
            }).isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Comportamento de Timestamps")
    class ComportamentoTimestampsTests {

        @Test
        @DisplayName("Deve manter data de criação inalterada ao atualizar")
        void deveManterDataCriacaoInalteradaAoAtualizar() {
            Tarefa tarefaSalva = entityManager.persistAndFlush(tarefa1);
            LocalDateTime dataCriacaoOriginal = tarefaSalva.getDataCriacao();
            entityManager.clear();

            tarefaSalva.setTitulo("Título Modificado");
            tarefaSalva.setDataAtualizacao(LocalDateTime.now());
            Tarefa tarefaAtualizada = tarefaRepository.save(tarefaSalva);

            assertThat(tarefaAtualizada.getDataCriacao()).isEqualTo(dataCriacaoOriginal);
            assertThat(tarefaAtualizada.getDataAtualizacao()).isNotNull();
        }

        @Test
        @DisplayName("Deve aceitar tarefa com descrição nula")
        void deveAceitarTarefaComDescricaoNula() {
            Tarefa tarefaComDescricaoNula = Tarefa.builder()
                    .titulo("Título sem Descrição")
                    .descricao(null)
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .dataCriacao(LocalDateTime.now())
                    .build();

            Tarefa tarefaSalva = tarefaRepository.save(tarefaComDescricaoNula);

            assertThat(tarefaSalva).isNotNull();
            assertThat(tarefaSalva.getId()).isNotNull();
            assertThat(tarefaSalva.getDescricao()).isNull();
        }
    }

    @Nested
    @DisplayName("Consultas Customizadas")
    class ConsultasCustomizadasTests {

        @Test
        @DisplayName("Deve contar total de tarefas")
        void deveContarTotalDeTarefas() {
            entityManager.persist(tarefa1);
            entityManager.persist(tarefa2);
            entityManager.flush();

            long total = tarefaRepository.count();

            assertThat(total).isEqualTo(2);
        }

        @Test
        @DisplayName("Deve retornar zero quando não há tarefas")
        void deveRetornarZeroQuandoNaoHaTarefas() {
            long total = tarefaRepository.count();

            assertThat(total).isZero();
        }
    }

    @Nested
    @DisplayName("Performance e Limpeza")
    class PerformanceLimpezaTests {

        @Test
        @DisplayName("Deve limpar todas as tarefas")
        void deveLimparTodasAsTarefas() {
            entityManager.persist(tarefa1);
            entityManager.persist(tarefa2);
            entityManager.persist(tarefa3);
            entityManager.flush();

            tarefaRepository.deleteAll();

            List<Tarefa> tarefasRestantes = tarefaRepository.findAll();
            assertThat(tarefasRestantes).isEmpty();
        }

        @Test
        @DisplayName("Deve executar operações em lote")
        void deveExecutarOperacoesEmLote() {
            List<Tarefa> tarefas = List.of(tarefa1, tarefa2, tarefa3);

            List<Tarefa> tarefasSalvas = tarefaRepository.saveAll(tarefas);

            assertThat(tarefasSalvas).hasSize(3);
            assertThat(tarefasSalvas).allMatch(tarefa -> tarefa.getId() != null);
        }
    }
}