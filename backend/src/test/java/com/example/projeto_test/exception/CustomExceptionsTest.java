package com.example.projeto_test.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Exceções Customizadas - Testes Unitários")
class CustomExceptionsTest {

    @Nested
    @DisplayName("TarefaNotFoundException")
    class TarefaNotFoundExceptionTests {

        @Test
        @DisplayName("Deve criar exceção com ID")
        void deveCriarExcecaoComId() {
            Long id = 1L;

            
            TarefaNotFoundException exception = new TarefaNotFoundException(id);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Tarefa com ID 1 não foi encontrada");
            assertThat(exception.getId()).isEqualTo(id);
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Deve ser lançável")
        void deveSerLancavel() {
            Long id = 999L;

            assertThatThrownBy(() -> {
                throw new TarefaNotFoundException(id);
            })
            .isInstanceOf(TarefaNotFoundException.class)
            .hasMessage("Tarefa com ID 999 não foi encontrada");
        }
    }

    @Nested
    @DisplayName("BusinessRuleException")
    class BusinessRuleExceptionTests {

        @Test
        @DisplayName("Deve criar exceção de regra de negócio")
        void deveCriarExcecaoDeRegraDeNegocio() {
           
            String mensagem = "Não é possível concluir uma tarefa já concluída";

            BusinessRuleException exception = new BusinessRuleException(mensagem);


            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(mensagem);
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Deve ser lançável com detalhes de negócio")
        void deveSerLancavelComDetalhesDeNegocio() {
            String mensagem = "Estado inválido para operação";

            assertThatThrownBy(() -> {
                throw new BusinessRuleException(mensagem);
            })
            .isInstanceOf(BusinessRuleException.class)
            .hasMessage(mensagem);
        }
    }

    @Nested
    @DisplayName("DataConflictException")
    class DataConflictExceptionTests {

        @Test
        @DisplayName("Deve criar exceção de conflito de dados")
        void deveCriarExcecaoDeConflitoDeDados() {
            String mensagem = "Violação de integridade referencial";

            DataConflictException exception = new DataConflictException(mensagem);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(mensagem);
            assertThat(exception).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Deve ser lançável em conflitos")
        void deveSerLancavelEmConflitos() {
            String mensagem = "Dados já existem no sistema";

            assertThatThrownBy(() -> {
                throw new DataConflictException(mensagem);
            })
            .isInstanceOf(DataConflictException.class)
            .hasMessage(mensagem);
        }
    }

    @Nested
    @DisplayName("ErrorResponse")
    class ErrorResponseTests {

        @Test
        @DisplayName("Deve criar resposta de erro completa")
        void deveCriarRespostaDeErroCompleta() {
            LocalDateTime timestamp = LocalDateTime.now();
            int status = 404;
            String error = "Não Encontrado";
            String message = "Recurso não encontrado";
            Map<String, String> details = Map.of("id", "1");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(timestamp)
                    .status(status)
                    .error(error)
                    .message(message)
                    .details(details)
                    .build();

            assertThat(errorResponse).isNotNull();
            assertThat(errorResponse.getTimestamp()).isEqualTo(timestamp);
            assertThat(errorResponse.getStatus()).isEqualTo(status);
            assertThat(errorResponse.getError()).isEqualTo(error);
            assertThat(errorResponse.getMessage()).isEqualTo(message);
            assertThat(errorResponse.getDetails()).isEqualTo(details);
        }

        @Test
        @DisplayName("Deve criar resposta de erro mínima")
        void deveCriarRespostaDeErroMinima() {
            int status = 400;
            String error = "Bad Request";

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(status)
                    .error(error)
                    .build();

            assertThat(errorResponse).isNotNull();
            assertThat(errorResponse.getStatus()).isEqualTo(status);
            assertThat(errorResponse.getError()).isEqualTo(error);
            assertThat(errorResponse.getMessage()).isNull();
            assertThat(errorResponse.getDetails()).isNull();
            assertThat(errorResponse).isNotNull();
            assertThat(errorResponse.getStatus()).isEqualTo(status);
            assertThat(errorResponse.getError()).isEqualTo(error);
            assertThat(errorResponse.getMessage()).isNull();
            assertThat(errorResponse.getDetails()).isNull();
        }

        @Test
        @DisplayName("Deve permitir details como Map")
        void devePermitirDetailsComoMap() {
            // Given
            Map<String, String> detailsMap = Map.of(
                    "campo", "titulo",
                    "erro", "não pode estar vazio"
            );

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(400)
                    .error("Erro de Validação")
                    .details(detailsMap)
                    .build();

            assertThat(errorResponse.getDetails()).isEqualTo(detailsMap);
            assertThat(errorResponse.getDetails()).isInstanceOf(Map.class);
        }

        @Test
        @DisplayName("Deve suportar equals e hashCode")
        void deveSuportarEqualsEHashCode() {
            ErrorResponse error1 = ErrorResponse.builder()
                    .status(404)
                    .error("Não Encontrado")
                    .message("Recurso não existe")
                    .build();

            ErrorResponse error2 = ErrorResponse.builder()
                    .status(404)
                    .error("Não Encontrado")
                    .message("Recurso não existe")
                    .build();

            ErrorResponse error3 = ErrorResponse.builder()
                    .status(400)
                    .error("Bad Request")
                    .build();

            assertThat(error1).isEqualTo(error2);
            assertThat(error1).isNotEqualTo(error3);
            assertThat(error1.hashCode()).isEqualTo(error2.hashCode());
        }

        @Test
        @DisplayName("Deve ter toString legível")
        void deveTerToStringLegivel() {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(404)
                    .error("Não Encontrado")
                    .message("Tarefa não encontrada")
                    .build();

            String toString = errorResponse.toString();

            assertThat(toString).contains("404");
            assertThat(toString).contains("Não Encontrado");
            assertThat(toString).contains("Tarefa não encontrada");
        }
    }
}