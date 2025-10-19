package com.example.projeto_test.integration;

import com.example.projeto_test.dto.TarefaDTO;
import com.example.projeto_test.infrastructure.entitys.Tarefa;
import com.example.projeto_test.infrastructure.entitys.repository.TarefaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Testes de Integração Completa - API de Tarefas")
class TarefaIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
        
        tarefaRepository.deleteAll();
    }

    @Nested
    @DisplayName("Fluxo Completo CRUD")
    class FluxoCompletoCrudTests {

        @Test
        @DisplayName("Deve executar fluxo completo de CRUD com sucesso")
        void deveExecutarFluxoCompletoDeCrudComSucesso() throws Exception {
            TarefaDTO novaTarefa = TarefaDTO.builder()
                    .titulo("Integração Completa")
                    .descricao("Teste de integração end-to-end")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            String tarefaCriada = mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(novaTarefa)))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.titulo", is("Integração Completa")))
                    .andExpect(jsonPath("$.status", is("PENDENTE")))
                    .andReturn().getResponse().getContentAsString();

            Long tarefaId = objectMapper.readTree(tarefaCriada).get("id").asLong();

            mockMvc.perform(get("/tasks"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(tarefaId.intValue())));

            mockMvc.perform(get("/tasks/{id}", tarefaId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(tarefaId.intValue())))
                    .andExpect(jsonPath("$.titulo", is("Integração Completa")));

            TarefaDTO tarefaAtualizada = TarefaDTO.builder()
                    .titulo("Integração Atualizada")
                    .descricao("Teste atualizado com sucesso")
                    .status(Tarefa.StatusTarefa.CONCLUIDA)
                    .build();

            mockMvc.perform(put("/tasks/{id}", tarefaId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaAtualizada)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.titulo", is("Integração Atualizada")))
                    .andExpect(jsonPath("$.status", is("CONCLUIDA")))
                    .andExpect(jsonPath("$.dataAtualizacao", notNullValue()));

            mockMvc.perform(delete("/tasks/{id}", tarefaId))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/tasks/{id}", tarefaId))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Cenários de Validação em Contexto Real")
    class CenariosValidacaoRealTests {

        @Test
        @DisplayName("Deve validar dados em contexto de aplicação real")
        void deveValidarDadosEmContextoDeAplicacaoReal() throws Exception {
            TarefaDTO tarefaInvalida = TarefaDTO.builder()
                    .titulo("")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaInvalida)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)))
                    .andExpect(jsonPath("$.error", is("Erro de Validação")));

            TarefaDTO tarefaTituloLongo = TarefaDTO.builder()
                    .titulo("A".repeat(101))
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaTituloLongo)))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details.titulo", containsString("entre 3 e 100 caracteres")));
        }

        @Test
        @DisplayName("Deve tratar erros 404 em cenários reais")
        void deveTratarErros404EmCenariosReais() throws Exception {
            mockMvc.perform(get("/tasks/999"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.message", containsString("ID 999 não foi encontrada")));

            TarefaDTO tarefaUpdate = TarefaDTO.builder()
                    .titulo("Não Existe")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            mockMvc.perform(put("/tasks/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaUpdate)))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            mockMvc.perform(delete("/tasks/999"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Persistência e Transações")
    class PersistenciaTransacoesTests {

        @Test
        @DisplayName("Deve garantir persistência através de transações")
        void deveGarantirPersistenciaAtravesDeTransacoes() throws Exception {
            for (int i = 1; i <= 3; i++) {
                TarefaDTO tarefa = TarefaDTO.builder()
                        .titulo("Tarefa " + i)
                        .descricao("Descrição da tarefa " + i)
                        .status(Tarefa.StatusTarefa.PENDENTE)
                        .build();

                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tarefa)))
                        .andExpect(status().isCreated());
            }

            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));

            long totalNoBanco = tarefaRepository.count();
            assertThat(totalNoBanco).isEqualTo(3);
        }

        @Test
        @DisplayName("Deve manter integridade referencial")
        void deveManterIntegridadeReferencial() throws Exception {
            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Tarefa para Integridade")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            String resposta = mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefa)))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getContentAsString();

            Long tarefaId = objectMapper.readTree(resposta).get("id").asLong();

            boolean existeNoBanco = tarefaRepository.existsById(tarefaId);
            assertThat(existeNoBanco).isTrue();

            mockMvc.perform(delete("/tasks/{id}", tarefaId))
                    .andExpect(status().isNoContent());

            boolean aindaExisteNoBanco = tarefaRepository.existsById(tarefaId);
            assertThat(aindaExisteNoBanco).isFalse();
        }
    }

    @Nested
    @DisplayName("Headers e Content-Type")
    class HeadersContentTypeTests {

        @Test
        @DisplayName("Deve validar Content-Type corretamente")
        void deveValidarContentTypeCorretamente() throws Exception {
            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("dados inválidos"))
                    .andDo(print())
                    .andExpect(status().isUnsupportedMediaType())
                    .andExpect(jsonPath("$.status", is(415)));

            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Teste Content-Type")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefa)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("Deve retornar headers apropriados")
        void deveRetornarHeadersApropriados() throws Exception {
            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            TarefaDTO tarefa = TarefaDTO.builder()
                    .titulo("Teste Headers")
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefa)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @DisplayName("Performance e Limites")
    class PerformanceLimitesTests {

        @Test
        @DisplayName("Deve lidar com múltiplas requisições")
        void deveLidarComMultiplasRequisicoes() throws Exception {
            for (int i = 1; i <= 10; i++) {
                TarefaDTO tarefa = TarefaDTO.builder()
                        .titulo("Performance Test " + i)
                        .status(Tarefa.StatusTarefa.PENDENTE)
                        .build();

                mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tarefa)))
                        .andExpect(status().isCreated());
            }

            mockMvc.perform(get("/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(10)));
        }

        @Test
        @DisplayName("Deve tratar limite de caracteres corretamente")
        void deveTratarLimiteDeCaracteresCorretamente() throws Exception {
            String tituloLimite = "A".repeat(100);
            TarefaDTO tarefaLimite = TarefaDTO.builder()
                    .titulo(tituloLimite)
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaLimite)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.titulo", is(tituloLimite)));

            String descricaoLimite = "B".repeat(500);
            TarefaDTO tarefaDescricaoLimite = TarefaDTO.builder()
                    .titulo("Título Normal")
                    .descricao(descricaoLimite)
                    .status(Tarefa.StatusTarefa.PENDENTE)
                    .build();

            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tarefaDescricaoLimite)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.descricao", is(descricaoLimite)));
        }
    }


}