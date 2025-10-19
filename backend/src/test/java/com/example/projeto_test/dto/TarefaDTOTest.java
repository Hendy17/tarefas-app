package com.example.projeto_test.dto;

import com.example.projeto_test.infrastructure.entitys.Tarefa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("TarefaDTO - Testes de Validação")
class TarefaDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Validação do Título")
    class ValidacaoTituloTests {

        @Test
        @DisplayName("Deve validar título válido")
        void deveValidarTituloValido() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Título Válido")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve rejeitar título nulo")
        void deveRejeitarTituloNulo() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo(null)
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).hasSize(1);
            ConstraintViolation<TarefaDTO> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("titulo");
            assertThat(violation.getMessage()).isEqualTo("Título é obrigatório e não pode estar vazio");
        }

        @Test
        @DisplayName("Deve rejeitar título vazio")
        void deveRejeitarTituloVazio() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

          
            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).hasSize(1);
            ConstraintViolation<TarefaDTO> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("titulo");
            assertThat(violation.getMessage()).isEqualTo("Título é obrigatório e não pode estar vazio");
        }

        @Test
        @DisplayName("Deve rejeitar título com apenas espaços")
        void deveRejeitarTituloComApenasEspacos() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("   ")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).hasSize(1);
            ConstraintViolation<TarefaDTO> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("titulo");
            assertThat(violation.getMessage()).isEqualTo("Título é obrigatório e não pode estar vazio");
        }

        @Test
        @DisplayName("Deve rejeitar título muito curto")
        void deveRejeitarTituloMuitoCurto() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Ab") 
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).hasSize(1);
            ConstraintViolation<TarefaDTO> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("titulo");
            assertThat(violation.getMessage()).isEqualTo("Título deve ter entre 3 e 100 caracteres");
        }

        @Test
        @DisplayName("Deve rejeitar título muito longo")
        void deveRejeitarTituloMuitoLongo() {
            String tituloLongo = "A".repeat(101); 
                    .titulo(tituloLongo)
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).hasSize(1);
            ConstraintViolation<TarefaDTO> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("titulo");
            assertThat(violation.getMessage()).isEqualTo("Título deve ter entre 3 e 100 caracteres");
        }

        @Test
        @DisplayName("Deve aceitar título no limite mínimo")
        void deveAceitarTituloNoLimiteMinimo() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("ABC") 
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar título no limite máximo")
        void deveAceitarTituloNoLimiteMaximo() {
            String tituloMaximo = "A".repeat(100); 
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo(tituloMaximo)
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("Validação da Descrição")
    class ValidacaoDescricaoTests {

        @Test
        @DisplayName("Deve aceitar descrição nula")
        void deveAceitarDescricaoNula() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Título Válido")
                    .descricao(null)
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar descrição válida")
        void deveAceitarDescricaoValida() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Título Válido")
                    .descricao("Descrição detalhada da tarefa")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve rejeitar descrição muito longa")
        void deveRejeitarDescricaoMuitoLonga() {
            String descricaoLonga = "A".repeat(501); 
                    .titulo("Título Válido")
                    .descricao(descricaoLonga)
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).hasSize(1);
            ConstraintViolation<TarefaDTO> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("descricao");
            assertThat(violation.getMessage()).isEqualTo("Descrição não pode ter mais de 500 caracteres");
        }
    }

    @Nested
    @DisplayName("Validação do Status")
    class ValidacaoStatusTests {

        @Test
        @DisplayName("Deve rejeitar status nulo")
        void deveRejeitarStatusNulo() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Título Válido")
                    .status(null)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).hasSize(1);
            ConstraintViolation<TarefaDTO> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("status");
            assertThat(violation.getMessage()).isEqualTo("Status é obrigatório");
        }

        @Test
        @DisplayName("Deve aceitar status PENDENTE")
        void deveAceitarStatusPendente() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Título Válido")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar status CONCLUIDA")
        void deveAceitarStatusConcluida() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Título Válido")
                    .status(Tarefa.StatusTarefa.CONCLUIDA)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar status CANCELADA")
        void deveAceitarStatusCancelada() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Título Válido")
                    .status(Tarefa.StatusTarefa.CANCELADA)
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("Validação Múltipla")
    class ValidacaoMultiplaTests {

        @Test
        @DisplayName("Deve retornar múltiplas violações")
        void deveRetornarMultiplasViolacoes() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("") 
                    .descricao("A".repeat(501)) 
                    .status(null) 
                    .build();

            Set<ConstraintViolation<TarefaDTO>> violations = validator.validate(tarefa);

            assertThat(violations).hasSize(3);

            boolean tituloViolation = violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("titulo"));
            boolean descricaoViolation = violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("descricao"));
            boolean statusViolation = violations.stream()
                    .anyMatch(v -> v.getPropertyPath().toString().equals("status"));

            assertThat(tituloViolation).isTrue();
            assertThat(descricaoViolation).isTrue();
            assertThat(statusViolation).isTrue();
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderPatternTests {

        @Test
        @DisplayName("Deve criar objeto usando builder")
        void deveCriarObjetoUsandoBuilder() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Teste Builder")
                    .descricao("Testando o padrão Builder")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            assertThat(tarefa).isNotNull();
            assertThat(tarefa.getTitulo()).isEqualTo("Teste Builder");
            assertThat(tarefa.getDescricao()).isEqualTo("Testando o padrão Builder");
            assertThat(tarefa.getStatus()).isEqualTo(Tarefa.StatusTarefa.PENDENTE);
        }

        @Test
        @DisplayName("Deve permitir builder parcial")
        void devePermitirBuilderParcial() {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Apenas Título")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            assertThat(tarefa).isNotNull();
            assertThat(tarefa).isNotNull();
            assertThat(tarefa.getTitulo()).isEqualTo("Apenas Título");
            assertThat(tarefa.getDescricao()).isNull();
            assertThat(tarefa.getStatus()).isEqualTo(Tarefa.StatusTarefa.PENDENTE);
        }
    }
}