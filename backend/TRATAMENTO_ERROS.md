# 🚨 Tratamento de Erros HTTP - Guia Completo

## 📋 Códigos de Status Implementados

### ✅ **2xx - Sucesso**
- **200 OK**: Operação realizada com sucesso
- **201 Created**: Recurso criado com sucesso  
- **204 No Content**: Recurso deletado com sucesso

### ❌ **4xx - Erros do Cliente**

#### **400 Bad Request**
- **Dados inválidos** (validação falhou)
- **JSON malformado** 
- **Tipos de parâmetros incorretos**
- **Parâmetros obrigatórios ausentes**

#### **404 Not Found**  
- **Tarefa não encontrada** por ID
- **Endpoint não existe**

#### **405 Method Not Allowed**
- **Método HTTP** não suportado para o endpoint

#### **409 Conflict**
- **Violação de integridade** de dados
- **Duplicidade** de registros únicos

#### **415 Unsupported Media Type**
- **Content-Type** não suportado (deve ser application/json)

#### **422 Unprocessable Entity**
- **Regras de negócio** violadas
- **Estado inválido** para operação

### ❌ **5xx - Erros do Servidor**

#### **500 Internal Server Error**
- **Erros inesperados** do sistema

---

## 🧪 Exemplos de Teste

### ✅ **Sucesso (200/201/204)**

```bash
# Criar tarefa válida
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Nova tarefa", "status": "PENDENTE"}'
# Resposta: 201 Created

# Listar tarefas  
curl http://localhost:8080/tasks
# Resposta: 200 OK

# Deletar tarefa
curl -X DELETE http://localhost:8080/tasks/1
# Resposta: 204 No Content
```

### ❌ **400 Bad Request - Validação**

```bash
# Título vazio
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"titulo": "", "status": "PENDENTE"}'
```
**Resposta:**
```json
{
  "timestamp": "2024-10-18T00:30:00",
  "status": 400,
  "error": "Erro de Validação",
  "message": "Dados inválidos fornecidos",
  "details": {
    "titulo": "Título é obrigatório e não pode estar vazio"
  }
}
```

```bash
# Título muito curto
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Ab", "status": "PENDENTE"}'
```
**Resposta:**
```json
{
  "timestamp": "2024-10-18T00:30:00", 
  "status": 400,
  "error": "Erro de Validação",
  "message": "Dados inválidos fornecidos",
  "details": {
    "titulo": "Título deve ter entre 3 e 100 caracteres"
  }
}
```

### ❌ **400 Bad Request - JSON Malformado**

```bash
# JSON inválido
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Teste", "status": PENDENTE}'  # Sem aspas no enum
```
**Resposta:**
```json
{
  "timestamp": "2024-10-18T00:30:00",
  "status": 400,
  "error": "JSON Inválido", 
  "message": "Valor inválido para campo enum",
  "details": {
    "campo": "status",
    "valorRecebido": "PENDENTE",
    "valoresValidos": "[PENDENTE, CONCLUIDA, CANCELADA]"
  }
}
```

### ❌ **400 Bad Request - Tipo Inválido**

```bash
# ID como string
curl http://localhost:8080/tasks/abc
```
**Resposta:**
```json
{
  "timestamp": "2024-10-18T00:30:00",
  "status": 400,
  "error": "Tipo de Parâmetro Inválido",
  "message": "O parâmetro 'id' deve ser do tipo Long",
  "details": {
    "parametro": "id",
    "valorRecebido": "abc", 
    "tipoEsperado": "Long"
  }
}
```

### ❌ **404 Not Found**

```bash
# Tarefa inexistente
curl http://localhost:8080/tasks/999
```
**Resposta:**
```json
{
  "timestamp": "2024-10-18T00:30:00",
  "status": 404,
  "error": "Tarefa Não Encontrada",
  "message": "Tarefa com ID 999 não foi encontrada", 
  "details": {
    "id": "999"
  }
}
```

```bash
# Endpoint inexistente
curl http://localhost:8080/inexistente
```
**Resposta:**
```json
{
  "timestamp": "2024-10-18T00:30:00",
  "status": 404,
  "error": "Endpoint Não Encontrado", 
  "message": "O endpoint 'GET /inexistente' não existe",
  "details": {
    "metodo": "GET",
    "url": "/inexistente"
  }
}
```

### ❌ **405 Method Not Allowed**

```bash
# Método não suportado
curl -X PATCH http://localhost:8080/tasks
```
**Resposta:**
```json
{
  "timestamp": "2024-10-18T00:30:00",
  "status": 405,
  "error": "Método Não Permitido",
  "message": "Método HTTP 'PATCH' não é suportado para esta URL",
  "details": {
    "metodoRecebido": "PATCH",
    "metodosSuportados": "GET, POST", 
    "url": "/tasks"
  }
}
```

### ❌ **415 Unsupported Media Type**

```bash
# Content-Type incorreto
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: text/plain" \
  -d "dados"
```
**Resposta:**
```json
{
  "timestamp": "2024-10-18T00:30:00",
  "status": 415,
  "error": "Tipo de Mídia Não Suportado", 
  "message": "O tipo de conteúdo da requisição não é suportado",
  "details": {
    "tipoRecebido": "text/plain",
    "tiposSuportados": "[application/json]"
  }
}
```

### ❌ **422 Unprocessable Entity - Regra de Negócio**

```bash
# Tentar concluir tarefa já concluída
curl -X PATCH http://localhost:8080/tasks/1/complete
```
**Resposta:**
```json
{
  "timestamp": "2024-10-18T00:30:00",
  "status": 422,
  "error": "Regra de Negócio Violada",
  "message": "A tarefa 'Estudar Spring' já está concluída", 
  "details": {
    "regra": "ALREADY_COMPLETED",
    "estadoAtual": "CONCLUIDA"
  }
}
```

### ❌ **500 Internal Server Error**

```bash
# Erro interno (simulado por falha no banco)
```
**Resposta:**
```json
{
  "timestamp": "2024-10-18T00:30:00",
  "status": 500,
  "error": "Erro Interno do Servidor",
  "message": "Ocorreu um erro inesperado",
  "details": {
    "exception": "DataAccessException"
  }
}
```

---

## 🛠️ **Novos Endpoints para Demonstração**

### **Busca por Status**
```bash
GET /tasks/status/{status}

# Exemplos:
curl http://localhost:8080/tasks/status/PENDENTE      # ✅ Válido
curl http://localhost:8080/tasks/status/INVALID      # ❌ 422 - Status inválido
```

### **Busca por Título**  
```bash
GET /tasks/search?titulo=termo

# Exemplos:
curl http://localhost:8080/tasks/search?titulo=Spring  # ✅ Válido
curl http://localhost:8080/tasks/search?titulo=A       # ❌ 422 - Muito curto  
```

### **Marcar como Concluída**
```bash
PATCH /tasks/{id}/complete

# Exemplos:
curl -X PATCH http://localhost:8080/tasks/1/complete   # ✅ Se pendente
curl -X PATCH http://localhost:8080/tasks/1/complete   # ❌ 422 - Se já concluída
```

### **Cancelar Tarefa**
```bash
PATCH /tasks/{id}/cancel

# Exemplos:
curl -X PATCH http://localhost:8080/tasks/1/cancel     # ✅ Se pendente
curl -X PATCH http://localhost:8080/tasks/1/cancel     # ❌ 422 - Se já concluída
```

---

## 🎯 **Resumo dos Benefícios**

✅ **Códigos HTTP apropriados** para cada situação  
✅ **Mensagens claras** e úteis para o cliente  
✅ **Detalhes estruturados** para debugging  
✅ **Tratamento global** sem código repetitivo  
✅ **Logs organizados** para monitoramento  
✅ **Validação robusta** em múltiplas camadas