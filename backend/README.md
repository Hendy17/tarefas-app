# � **Backend - API REST Tarefas**

Backend Java Spring Boot para o sistema de gerenciamento de tarefas.

---

## 🎯 **Visão Geral**

API REST robusta desenvolvida com **Spring Boot 3.5.6** e **Java 25**, oferecendo:
- ✅ CRUD completo de tarefas
- ✅ Validação robusta de dados
- ✅ Tratamento global de exceções
- ✅ Banco H2 em memória
- ✅ Testes unitários e de integração
- ✅ Logs estruturados
- ✅ CORS configurado

---

## 🚀 **Início Rápido**

### **Execução**
```bash
# Usando Maven wrapper (recomendado)
./mvnw spring-boot:run

# API disponível em: http://localhost:8080
# Console H2: http://localhost:8080/h2-console
```

### **Build**
```bash
# Compilar e gerar JAR
./mvnw clean package -DskipTests

# JAR gerado em: target/projeto-test-0.0.1-SNAPSHOT.jar
```

### **Testes**
```bash
# Executar todos os testes
./mvnw test
```

---

## 📋 **Endpoints da API**

### **Base URL**: `http://localhost:8080`

| HTTP | Endpoint | Descrição | Response |
|------|----------|-----------|----------|
| `GET` | `/tasks` | Listar todas as tarefas | `200` Lista de tarefas |
| `GET` | `/tasks/{id}` | Buscar tarefa por ID | `200` Tarefa / `404` Não encontrada |
| `POST` | `/tasks` | Criar nova tarefa | `201` Criada / `400` Dados inválidos |
| `PUT` | `/tasks/{id}` | Atualizar tarefa | `200` Atualizada / `404` Não encontrada |
| `DELETE` | `/tasks/{id}` | Deletar tarefa | `204` Removida / `404` Não encontrada |

---

## 📊 **Modelos de Dados**

### **TarefaDTO (Request)**
```json
{
  "titulo": "string (obrigatório, 3-100 chars)",
  "descricao": "string (opcional, max 1000 chars)",
  "status": "PENDENTE | CONCLUIDA | CANCELADA"
}
```

### **TarefaResponseDTO (Response)**
```json
{
  "id": 1,
  "titulo": "Implementar API",
  "descricao": "Desenvolver endpoints REST",
  "status": "PENDENTE",
  "dataCriacao": "2025-10-19T10:30:00",
  "dataAtualizacao": "2025-10-19T10:30:00"
}
```

---

## 🏗️ **Arquitetura**

### **Estrutura de Camadas**
```
com.example.projeto_test/
├── 🎮 controller/              # API Layer
├── 💼 business/                # Business Layer
├── 🏗️ infrastructure/          # Data Layer
├── 📦 dto/                     # Data Transfer Objects
├── 🚨 exception/               # Exception Handling
└── ProjetoTestApplication.java # Main Class
```

---

## 🗄️ **Banco de Dados H2**

### **Acesso ao Console**
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:tarefasdb`
- **Username**: `sa`
- **Password**: (vazio)

---

## 🛡️ **Validações**

### **Regras de Negócio**
- **Título**: Obrigatório, 3-100 caracteres
- **Descrição**: Opcional, máximo 1000 caracteres
- **Status**: PENDENTE, CONCLUIDA, CANCELADA

---

## 🧪 **Testes**

```bash
# Todos os testes
./mvnw test

# Testes específicos
./mvnw test -Dtest=TarefaServiceTest
./mvnw test -Dtest=TarefaControllerTest
./mvnw test -Dtest=TarefaIntegrationTest
```

---

## 🔧 **Tecnologias**

| Tecnologia | Versão | Descrição |
|-----------|--------|-----------|
| **Java** | 25 | Linguagem principal |
| **Spring Boot** | 3.5.6 | Framework web |
| **Spring Data JPA** | - | Persistência |
| **H2 Database** | - | Banco em memória |
| **JUnit 5** | - | Testes |
| **Mockito** | - | Mocks |

**🔧 Backend desenvolvido com ❤️ usando Spring Boot**

## 🏗️ **Arquitetura**

### **Estrutura do Projeto**
```
📁 src/main/java/com/example/projeto_test/
├── 🎯 controller/          # Camada de apresentação (REST Controllers)
│   ├── TarefaController.java
│   └── TarefaSearchController.java
├── 🏢 buisness/           # Camada de negócio (Services)
│   └── TarefaService.java
├── 📊 dto/                # Data Transfer Objects
│   ├── TarefaDTO.java
│   └── TarefaResponseDTO.java
├── ❌ exception/          # Tratamento de exceções
│   ├── GlobalExceptionHandler.java
│   ├── ErrorResponse.java
│   ├── TarefaNotFoundException.java
│   ├── BusinessRuleException.java
│   └── DataConflictException.java
└── 🗄️ infrastructure/     # Camada de dados
    └── entitys/
        ├── Tarefa.java
        └── repository/
            └── TarefaRepository.java
```

### **Padrões Implementados**
- **MVC** - Model-View-Controller
- **DTO** - Data Transfer Objects para separação de camadas  
- **Repository** - Acesso a dados com Spring Data JPA
- **Builder** - Para construção de objetos complexos
- **Global Exception Handler** - Tratamento centralizado de exceções

---

## ⚙️ **Tecnologias Utilizadas**

### **Core Framework**
- ☕ **Java 25** - Linguagem de programação
- 🍃 **Spring Boot 3.5.6** - Framework principal
- 🌐 **Spring Web** - APIs REST
- 💾 **Spring Data JPA** - Persistência de dados
- ✅ **Spring Validation** - Validação de dados

### **Banco de Dados**
- 🗄️ **H2 Database** - Banco em memória para desenvolvimento
- 🔗 **Hibernate** - ORM (Object-Relational Mapping)
- 📊 **HikariCP** - Pool de conexões

### **Ferramentas de Desenvolvimento**
- 🏗️ **Maven** - Gerenciamento de dependências
- 📝 **Lombok** - Redução de boilerplate
- 🧪 **JUnit 5** - Framework de testes
- 🎭 **Mockito** - Mocking para testes
- 📋 **AssertJ** - Assertions fluentes

---

## 🎯 **Endpoints da API**

### **📋 Operações CRUD Principais**

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| `GET` | `/tasks` | Lista todas as tarefas | 200 |
| `POST` | `/tasks` | Cria nova tarefa | 201 |
| `GET` | `/tasks/{id}` | Busca tarefa por ID | 200/404 |
| `PUT` | `/tasks/{id}` | Atualiza tarefa completa | 200/404 |
| `DELETE` | `/tasks/{id}` | Remove tarefa | 204/404 |

### **🔍 Operações de Busca Avançada**

| Método | Endpoint | Descrição | Exemplo |
|--------|----------|-----------|---------|
| `GET` | `/tasks/status/{status}` | Busca por status | `/tasks/status/PENDENTE` |
| `GET` | `/tasks/search?titulo=termo` | Busca por título | `/tasks/search?titulo=Spring` |

### **⚡ Operações de Estado**

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|--------|
| `PATCH` | `/tasks/{id}/complete` | Marca como concluída | 200/422 |
| `PATCH` | `/tasks/{id}/cancel` | Cancela tarefa | 200/422 |

---

## 📝 **Modelo de Dados**

### **Tarefa (Entity)**
```json
{
  "id": 1,
  "titulo": "Implementar API REST",
  "descricao": "Criar endpoints para gerenciamento de tarefas",
  "status": "PENDENTE",
  "dataCriacao": "2024-10-18T10:30:00",
  "dataAtualizacao": "2024-10-18T11:45:00"
}
```

### **Status Disponíveis**
- 🟡 **PENDENTE** - Tarefa criada e aguardando execução
- 🟢 **CONCLUIDA** - Tarefa finalizada com sucesso
- 🔴 **CANCELADA** - Tarefa cancelada/abandonada

### **Regras de Validação**
- **Título**: Obrigatório, 3-100 caracteres
- **Descrição**: Opcional, máximo 500 caracteres  
- **Status**: Obrigatório, valores do enum
- **Data Criação**: Automática na criação
- **Data Atualização**: Automática na modificação

---

## 🚀 **Como Executar**

### **📋 Pré-requisitos**
- ☕ Java 25+ instalado
- 🏗️ Maven 3.9+ ou usar o wrapper incluído
- 🌐 Porta 8080 disponível

### **⚡ Execução Rápida**

```bash
# 1. Clone/baixe o projeto
cd projeto-test

# 2. Execute a aplicação
./mvnw spring-boot:run

# 3. Acesse no navegador
open http://localhost:8080/tasks
```

### **🔧 Comandos Úteis**

```bash
# Compilar o projeto
./mvnw clean compile

# Executar testes
./mvnw test

# Gerar JAR executável
./mvnw clean package

# Executar JAR
java -jar target/projeto-test-0.0.1-SNAPSHOT.jar

# Limpar build
./mvnw clean
```

---

## 📊 **Console H2 Database**

### **Acesso ao Banco**
- 🌐 **URL**: http://localhost:8080/h2-console
- 🔗 **JDBC URL**: `jdbc:h2:mem:tarefasdb`
- 👤 **Usuario**: `SA`
- 🔑 **Senha**: *(vazio)*

### **Consultas Úteis**
```sql
-- Listar todas as tarefas
SELECT * FROM tarefas ORDER BY data_criacao DESC;

-- Contar tarefas por status
SELECT status, COUNT(*) as total FROM tarefas GROUP BY status;

-- Tarefas criadas hoje
SELECT * FROM tarefas WHERE DATE(data_criacao) = CURRENT_DATE;
```

---

## 🧪 **Exemplos de Uso**

### **📝 Criar Nova Tarefa**
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Estudar Spring Boot",
    "descricao": "Aprender conceitos avançados de Spring Boot 3",
    "status": "PENDENTE"
  }'
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "titulo": "Estudar Spring Boot",
  "descricao": "Aprender conceitos avançados de Spring Boot 3",
  "status": "PENDENTE",
  "dataCriacao": "2024-10-18T10:30:00",
  "dataAtualizacao": null
}
```

### **📋 Listar Todas as Tarefas**
```bash
curl http://localhost:8080/tasks
```

### **🔍 Buscar Tarefa por ID**
```bash
curl http://localhost:8080/tasks/1
```

### **✏️ Atualizar Tarefa**
```bash
curl -X PUT http://localhost:8080/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Estudar Spring Boot - Concluído",
    "descricao": "Estudos finalizados com sucesso",
    "status": "CONCLUIDA"
  }'
```

### **✅ Marcar como Concluída**
```bash
curl -X PATCH http://localhost:8080/tasks/1/complete
```

### **🗑️ Deletar Tarefa**
```bash
curl -X DELETE http://localhost:8080/tasks/1
```

---

## ❌ **Tratamento de Erros**

### **Códigos HTTP Implementados**

| Status | Situação | Exemplo |
|--------|----------|---------|
| **200** | Sucesso | Operação realizada |
| **201** | Criado | Tarefa criada |
| **204** | Sem conteúdo | Tarefa deletada |
| **400** | Dados inválidos | Validação falhou |
| **404** | Não encontrado | Tarefa inexistente |
| **405** | Método não permitido | POST em endpoint GET |
| **409** | Conflito | Violação de integridade |
| **415** | Mídia não suportada | Content-Type inválido |
| **422** | Regra de negócio | Estado inválido |
| **500** | Erro interno | Falha inesperada |

### **Formato de Erro Padronizado**
```json
{
  "timestamp": "2024-10-18T10:30:00",
  "status": 400,
  "error": "Erro de Validação",
  "message": "Dados inválidos fornecidos",
  "details": {
    "titulo": "Título é obrigatório e não pode estar vazio"
  }
}
```

---

## 🧪 **Testes**

### **📊 Cobertura de Testes**
- **80+ Testes** unitários e de integração
- **6 Classes** de teste especializadas
- **Cobertura completa** de todas as camadas
- **Cenários de sucesso** e erro testados

### **🏗️ Estrutura de Testes**
```
📁 src/test/java/
├── 🎯 controller/TarefaControllerTest.java      # Testes de API
├── 🏢 buisness/TarefaServiceTest.java           # Testes de regras
├── 📊 dto/TarefaDTOTest.java                    # Testes de validação
├── 🗄️ repository/TarefaRepositoryTest.java      # Testes de dados
├── ❌ exception/CustomExceptionsTest.java       # Testes de exceções
└── 🔄 integration/TarefaIntegrationTest.java   # Testes end-to-end
```

### **▶️ Executar Testes**
```bash
# Todos os testes
./mvnw test

# Testes específicos
./mvnw test -Dtest=TarefaServiceTest
./mvnw test -Dtest="*ControllerTest"

# Com relatórios
./mvnw test jacoco:report
```

---

## 📚 **Documentação**

### **📁 Arquivos de Documentação**
- 📋 **README.md** - Este guia principal
- 🧪 **TESTES.md** - Documentação detalhada dos testes
- ✅ **VALIDACOES.md** - Guia de validações implementadas
- ❌ **TRATAMENTO_ERROS.md** - Manual de tratamento de erros

### **🔗 Links Úteis**
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Bean Validation](https://beanvalidation.org/)
- [H2 Database](https://www.h2database.com/)

---

## 🎯 **Recursos Implementados**

### ✅ **Funcionalidades Principais**
- [x] **CRUD Completo** de tarefas
- [x] **Validação Bean Validation** robusta  
- [x] **Tratamento de Erros** com códigos HTTP apropriados
- [x] **Persistência H2** com JPA/Hibernate
- [x] **Testes Unitários** com JUnit e Mockito
- [x] **Testes de Integração** end-to-end
- [x] **Documentação Completa** e exemplos
- [x] **Console H2** para debug e desenvolvimento

### 🚀 **Recursos Avançados**
- [x] **DTOs** para separação de camadas
- [x] **Global Exception Handler** centralizado
- [x] **Builder Pattern** para objetos complexos
- [x] **Timestamps** automáticos (criação/atualização)
- [x] **Validação de Status** com enum
- [x] **Logs estruturados** para debug
- [x] **Transações** gerenciadas automaticamente

---

## 🤝 **Contribuição**

### **📝 Como Contribuir**
1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

### **📋 Diretrizes**
- Mantenha o padrão de código existente
- Adicione testes para novas funcionalidades
- Atualize a documentação conforme necessário
- Use mensagens de commit descritivas

---

## 📄 **Licença**

Este projeto está licenciado sob a **MIT License** - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

## 👥 **Autores**

- **Desenvolvedor** - *Implementação inicial* - [GitHub](https://github.com/)

---

## 🙏 **Agradecimentos**

- Spring Boot Team pelo excelente framework
- Comunidade Java pelos recursos e documentação
- Contributors e desenvolvedores que inspiraram este projeto

---

<div align="center">

**🚀 Feito com ❤️ e ☕ usando Spring Boot**

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-25-blue.svg)](https://openjdk.java.net/)
[![H2](https://img.shields.io/badge/H2-Database-orange.svg)](https://www.h2database.com/)
[![Tests](https://img.shields.io/badge/Tests-80%2B-green.svg)](#testes)

</div>