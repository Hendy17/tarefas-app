# 📋 Validação de Dados - Documentação de Teste

## ✅ Validações Implementadas

### 🔤 **Título da Tarefa**
- **Obrigatório**: `@NotBlank` - não pode ser vazio ou null
- **Tamanho**: `@Size(min=3, max=100)` - entre 3 e 100 caracteres  
- **Formato**: `@Pattern` - apenas letras, números, pontuação e espaços
- **Limpeza**: Automática remoção de espaços extras

### 📝 **Descrição da Tarefa**  
- **Opcional**: pode ser null ou vazia
- **Tamanho máximo**: `@Size(max=1000)` - até 1000 caracteres
- **Limpeza**: Automática remoção de espaços extras

### 🚦 **Status da Tarefa**
- **Obrigatório**: `@NotNull` - deve ter um status válido
- **Valores válidos**: PENDENTE, CONCLUIDA, CANCELADA
- **Padrão**: PENDENTE se não informado

## 🧪 Exemplos de Teste

### ✅ **Requisição Válida**
```json
POST /tasks
{
  "titulo": "Estudar Spring Boot",
  "descricao": "Aprender validações com Bean Validation",
  "status": "PENDENTE"
}
```

### ❌ **Requisições Inválidas**

#### Título vazio:
```json
POST /tasks
{
  "titulo": "",
  "status": "PENDENTE"
}
```
**Resposta**: 400 Bad Request - "Título é obrigatório e não pode estar vazio"

#### Título muito curto:
```json  
POST /tasks
{
  "titulo": "Ab",
  "status": "PENDENTE"
}
```
**Resposta**: 400 Bad Request - "Título deve ter entre 3 e 100 caracteres"

#### Status inválido:
```json
POST /tasks  
{
  "titulo": "Tarefa teste",
  "status": null
}
```
**Resposta**: 400 Bad Request - "Status é obrigatório"

#### Descrição muito longa:
```json
POST /tasks
{
  "titulo": "Tarefa teste",
  "descricao": "Texto com mais de 1000 caracteres...",
  "status": "PENDENTE"
}
```
**Resposta**: 400 Bad Request - "Descrição não pode exceder 1000 caracteres"

## 🔧 **Como Testar**

1. **Inicie a aplicação**:
```bash
mvn spring-boot:run
```

2. **Teste com curl**:
```bash
# Teste válido
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Nova tarefa", "status": "PENDENTE"}'

# Teste inválido - título vazio  
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"titulo": "", "status": "PENDENTE"}'
```

3. **Acesse o console H2**:
- URL: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:tarefasdb
- Username: sa
- Password: (vazio)

## 🎯 **Recursos de Validação**

✅ **Bean Validation** com anotações  
✅ **DTOs separados** para entrada e saída  
✅ **Tratamento global de exceções**  
✅ **Logs detalhados** para auditoria  
✅ **Respostas de erro padronizadas**  
✅ **Transações** com rollback automático  
✅ **Validação no banco** com constraints JPA