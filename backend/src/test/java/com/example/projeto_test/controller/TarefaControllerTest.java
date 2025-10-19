package com.example.projeto_test.controller;

import com.example.projeto_test.buisness.TarefaService;
import com.example.projeto_test.dto.TarefaDTO;
import com.example.projeto_test.dto.TarefaResponseDTO;
import com.example.projeto_test.exception.TarefaNotFoundException;
import com.example.projeto_test.infrastructure.entitys.Tarefa;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)
@DisplayName("TarefaController - Testes de Integração")
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TarefaService tarefaService;

    @Autowired
    private ObjectMapper objectMapper;

    private TarefaDTO tarefaDTO;
    private TarefaResponseDTO tarefaResponseDTO;
    private final Long TAREFA_ID = 1L;

    @BeforeEach
    void setUp() {
        tarefaDTO = TarefaDTO.builder()
                .titulo("Implementar Testes")
                .descricao("Criar testes unitários com JUnit e Mockito")
                .status(Tarefa.StatusTarefa.PENDENTE)
                .build();

        tarefaResponseDTO = TarefaResponseDTO.builder()
                .id(TAREFA_ID)
                .titulo("Implementar Testes")
                .descricao("Criar testes unitários com JUnit e Mockito")
                .status(Tarefa.StatusTarefa.PENDENTE)
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("POST /tasks - Criar Tarefa")
    class CriarTarefaTests {

        @Test
        @DisplayName("Deve criar tarefa com sucesso - 201")
        void deveCriarTarefaComSucesso() throws Exception {
            when(tarefaService.criarTarefa(any(TarefaDTO.class))).thenReturn(tarefaResponseDTO);

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaDTO)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(TAREFA_ID.intValue())))
                    .andExpect(jsonPath("$.titulo", is("Implementar Testes")))
                    .andExpect(jsonPath("$.descricao", is("Criar testes unitários com JUnit e Mockito")))
                    .andExpect(jsonPath("$.status", is("PENDENTE")))
                    .andExpect(jsonPath("$.dataCriacao", notNullValue()));

            verify(tarefaService, times(1)).criarTarefa(any(TarefaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando título estiver vazio")
        void deveRetornar400QuandoTituloEstiverVazio() throws Exception {
            tarefaDTO.setTitulo("");

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaDTO)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Erro de Validação")))
                    .andExpect(jsonPath("$.details.titulo", notNullValue()));

            verify(tarefaService, never()).criarTarefa(any(TarefaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando título for muito curto")
        void deveRetornar400QuandoTituloForMuitoCurto() throws Exception {
            tarefaDTO.setTitulo("Ab"); 

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaDTO)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.details.titulo", containsString("entre 3 e 100 caracteres")));

            verify(tarefaService, never()).criarTarefa(any(TarefaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando status for nulo")
        void deveRetornar400QuandoStatusForNulo() throws Exception {
            tarefaDTO.setStatus(null);

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaDTO)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.details.status", notNullValue()));

            verify(tarefaService, never()).criarTarefa(any(TarefaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 415 quando Content-Type for inválido")
        void deveRetornar415QuandoContentTypeForInvalido() throws Exception {
            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("dados invalidos"))
                    .andDo(print())
                    .andExpect(status().isUnsupportedMediaType())
                    .andExpect(jsonPath("$.status", is(415)))
                    .andExpect(jsonPath("$.error", is("Tipo de Mídia Não Suportado")));

            verify(tarefaService, never()).criarTarefa(any(TarefaDTO.class));
        }
    }

    @Nested
    @DisplayName("GET /tasks - Listar Tarefas")
    class ListarTarefasTests {

        @Test
        @DisplayName("Deve listar tarefas com sucesso - 200")
        void deveListarTarefasComSucesso() throws Exception {
            TarefaResponseDTO tarefa2 = TarefaResponseDTO.builder()
                    .id(2L)
                    .titulo("Segunda Tarefa")
                    .status(Tarefa.StatusTarefa.CONCLUIDA)
                    .dataCriacao(LocalDateTime.now())
                    .build();

            List<TarefaResponseDTO> tarefas = Arrays.asList(tarefaResponseDTO, tarefa2);
            when(tarefaService.listarTarefas()).thenReturn(tarefas);

            mockMvc.perform(get("/tasks"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].titulo", is("Implementar Testes")))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].titulo", is("Segunda Tarefa")));

            verify(tarefaService, times(1)).listarTarefas();
        }

        @Test
        @DisplayName("Deve retornar lista vazia com sucesso - 200")
        void deveRetornarListaVaziaComSucesso() throws Exception {
            when(tarefaService.listarTarefas()).thenReturn(Arrays.asList());

            mockMvc.perform(get("/tasks"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(tarefaService, times(1)).listarTarefas();
        }
    }

    @Nested
    @DisplayName("GET /tasks/{id} - Buscar Tarefa por ID")
    class BuscarTarefaPorIdTests {

        @Test
        @DisplayName("Deve buscar tarefa por ID com sucesso - 200")
        void deveBuscarTarefaPorIdComSucesso() throws Exception {
          
            when(tarefaService.buscarTarefaPorId(TAREFA_ID)).thenReturn(tarefaResponseDTO);

            mockMvc.perform(get("/tasks/{id}", TAREFA_ID))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(TAREFA_ID.intValue())))
                    .andExpect(jsonPath("$.titulo", is("Implementar Testes")));

            verify(tarefaService, times(1)).buscarTarefaPorId(TAREFA_ID);
        }

        @Test
        @DisplayName("Deve retornar 404 quando tarefa não for encontrada")
        void deveRetornar404QuandoTarefaNaoForEncontrada() throws Exception {
            when(tarefaService.buscarTarefaPorId(TAREFA_ID))
                    .thenThrow(new TarefaNotFoundException(TAREFA_ID));

            mockMvc.perform(get("/tasks/{id}", TAREFA_ID))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.error", is("Tarefa Não Encontrada")))
                    .andExpect(jsonPath("$.message", is("Tarefa com ID 1 não foi encontrada")));

            verify(tarefaService, times(1)).buscarTarefaPorId(TAREFA_ID);
        }

        @Test
        @DisplayName("Deve retornar 400 quando ID for inválido")
        void deveRetornar400QuandoIdForInvalido() throws Exception {
            mockMvc.perform(get("/tasks/{id}", "abc"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Tipo de Parâmetro Inválido")));

            verify(tarefaService, never()).buscarTarefaPorId(any());
        }
    }

    @Nested
    @DisplayName("PUT /tasks/{id} - Atualizar Tarefa")
    class AtualizarTarefaTests {

        @Test
        @DisplayName("Deve atualizar tarefa com sucesso - 200")
        void deveAtualizarTarefaComSucesso() throws Exception {
            TarefaResponseDTO tarefaAtualizada = TarefaResponseDTO.builder()
                    .id(TAREFA_ID)
                    .titulo("Título Atualizado")
                    .descricao("Descrição Atualizada")
                    .status(Tarefa.StatusTarefa.CONCLUIDA)
                    .dataCriacao(LocalDateTime.now().minusDays(1))
                    .dataAtualizacao(LocalDateTime.now())
                    .build();

            when(tarefaService.atualizarTarefa(eq(TAREFA_ID), any(TarefaDTO.class)))
                    .thenReturn(tarefaAtualizada);

            mockMvc.perform(put("/tasks/{id}", TAREFA_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaDTO)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(TAREFA_ID.intValue())))
                    .andExpect(jsonPath("$.titulo", is("Título Atualizado")))
                    .andExpect(jsonPath("$.dataAtualizacao", notNullValue()));

            verify(tarefaService, times(1)).atualizarTarefa(eq(TAREFA_ID), any(TarefaDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 404 ao tentar atualizar tarefa inexistente")
        void deveRetornar404AoTentarAtualizarTarefaInexistente() throws Exception {
            when(tarefaService.atualizarTarefa(eq(TAREFA_ID), any(TarefaDTO.class)))
                    .thenThrow(new TarefaNotFoundException(TAREFA_ID));

            mockMvc.perform(put("/tasks/{id}", TAREFA_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaDTO)))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.error", is("Tarefa Não Encontrada")));

            verify(tarefaService, times(1)).atualizarTarefa(eq(TAREFA_ID), any(TarefaDTO.class));
        }
    }

    @Nested
    @DisplayName("DELETE /tasks/{id} - Deletar Tarefa")
    class DeletarTarefaTests {

        @Test
        @DisplayName("Deve deletar tarefa com sucesso - 204")
        void deveDeletarTarefaComSucesso() throws Exception {
            doNothing().when(tarefaService).deletarTarefa(TAREFA_ID);

            mockMvc.perform(delete("/tasks/{id}", TAREFA_ID))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            verify(tarefaService, times(1)).deletarTarefa(TAREFA_ID);
        }

        @Test
        @DisplayName("Deve retornar 404 ao tentar deletar tarefa inexistente")
        void deveRetornar404AoTentarDeletarTarefaInexistente() throws Exception {
            doThrow(new TarefaNotFoundException(TAREFA_ID))
                    .when(tarefaService).deletarTarefa(TAREFA_ID);

            mockMvc.perform(delete("/tasks/{id}", TAREFA_ID))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.error", is("Tarefa Não Encontrada")));

            verify(tarefaService, times(1)).deletarTarefa(TAREFA_ID);
        }
    }

    @Nested
    @DisplayName("Métodos HTTP não permitidos")
    class MetodosNaoPermitidosTests {

        @Test
        @DisplayName("Deve retornar 405 para PATCH em /tasks")
        void deveRetornar405ParaPatchEmTasks() throws Exception {
            mockMvc.perform(patch("/tasks"))
                    .andDo(print())
                    .andExpect(status().isMethodNotAllowed())
                    .andExpect(jsonPath("$.status", is(405)))
                    .andExpect(jsonPath("$.error", is("Método Não Permitido")));

            verifyNoInteractions(tarefaService);
        }
    }
}