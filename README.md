
# 🚀 **Tarefas App - Sistema Fullstack de Gerenciamento de Tarefas**

<div align="center">

![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?style=for-the-badge&logo=spring)
![React](https://img.shields.io/badge/React-18.3.1-blue?style=for-the-badge&logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-4.9-blue?style=for-the-badge&logo=typescript)
![H2](https://img.shields.io/badge/H2-Database-blue?style=for-the-badge&logo=database)

**Sistema completo de gerenciamento de tarefas com backend Java Spring Boot e frontend React TypeScript**

[🎯 Features](#-features) •
[🚀 Início Rápido](#-início-rápido) •
[📋 API](#-api-endpoints) •
[🏗️ Arquitetura](#️-arquitetura) •
[🧪 Testes](#-testes)

</div>

---

## 🎯 **Features Implementadas**

### ✅ **Backend (Java Spring Boot)**
- **CRUD Completo** de tarefas (Create, Read, Update, Delete)
- **API REST** com endpoints padronizados
- **Validação robusta** de dados com Bean Validation
- **Tratamento global** de exceções com códigos HTTP apropriados
- **Banco H2** em memória para desenvolvimento
- **Logs estruturados** com SLF4J
- **CORS configurado** para frontend
- **Testes unitários** e de integração completos
- **Documentação** técnica detalhada

### ✅ **Frontend (React TypeScript)**
- **Interface moderna** e responsiva
- **Arquitetura modular** com componentes reutilizáveis
- **Gerenciamento de estado** com hooks customizados
- **Integração completa** com API backend
- **Validação** de formulários
- **Feedback visual** para usuário
- **Design system** consistente
- **TypeScript** para type safety

---

## 🚀 **Início Rápido**

### **📋 Pré-requisitos**

```bash
# Java 25+ (recomendado: OpenJDK)
java --version

# Node.js 18+ e npm
node --version
npm --version

# Maven 3.8+ (ou usar wrapper incluído)
mvn --version
```

### **🔥 Execução em Desenvolvimento**

#### **1️⃣ Backend (API Spring Boot)**

```bash
# 📁 Navegue para o backend
cd tarefas-app/backend

# 🔧 Compile e execute (usando Maven wrapper)
./mvnw spring-boot:run

# ✅ API disponível em: http://localhost:8080
# ✅ Console H2: http://localhost:8080/h2-console
# ✅ JDBC URL: jdbc:h2:mem:tarefasdb
# ✅ Username: sa | Password: (vazio)
```

#### **2️⃣ Frontend (React App)**

```bash
# 📁 Em novo terminal, navegue para frontend
cd tarefas-app/frontend

# 📦 Instale dependências
npm install

# 🎨 Execute em modo desenvolvimento
npm start

# ✅ App disponível em: http://localhost:3000
# ✅ Hot reload habilitado
```

### **🏗️ Build para Produção**

```bash
# Backend - Gerar JAR
cd backend
./mvnw clean package -DskipTests
# 📦 JAR: target/projeto-test-0.0.1-SNAPSHOT.jar

# Frontend - Build otimizado
cd frontend
npm run build
# 📦 Build: build/
```

---

## 📋 **API Endpoints**

### **Base URL:** `http://localhost:8080`

| Método | Endpoint | Descrição | Status Code |
|--------|----------|-----------|-------------|
| `GET` | `/tasks` | Listar todas as tarefas | `200 OK` |
| `GET` | `/tasks/{id}` | Buscar tarefa por ID | `200 OK` / `404 Not Found` |
| `POST` | `/tasks` | Criar nova tarefa | `201 Created` / `400 Bad Request` |
| `PUT` | `/tasks/{id}` | Atualizar tarefa | `200 OK` / `404 Not Found` |
| `DELETE` | `/tasks/{id}` | Deletar tarefa | `204 No Content` / `404 Not Found` |

### **📝 Exemplos de Uso**

#### **Criar Tarefa**
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Implementar API",
    "descricao": "Desenvolver endpoints REST",
    "status": "PENDENTE"
  }'
```

#### **Listar Tarefas**
```bash
curl -X GET http://localhost:8080/tasks
```

#### **Buscar por ID**
```bash
curl -X GET http://localhost:8080/tasks/1
```

#### **Atualizar Tarefa**
```bash
curl -X PUT http://localhost:8080/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "API Finalizada",
    "descricao": "Todos endpoints funcionando",
    "status": "CONCLUIDA"
  }'
```

#### **Deletar Tarefa**
```bash
curl -X DELETE http://localhost:8080/tasks/1
```

### **� Modelo de Dados**

#### **Tarefa (Task)**
```json
{
  "id": 1,
  "titulo": "Título da tarefa",
  "descricao": "Descrição detalhada",
  "status": "PENDENTE | CONCLUIDA | CANCELADA",
  "dataCriacao": "2025-10-19T10:30:00",
  "dataAtualizacao": "2025-10-19T10:30:00"
}
```

#### **Status Disponíveis**
- `PENDENTE` - Tarefa criada, aguardando execução
- `CONCLUIDA` - Tarefa finalizada com sucesso
- `CANCELADA` - Tarefa cancelada/rejeitada

---

## 🏗️ **Arquitetura do Sistema**

### **� Visão Geral**
```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐
│                 │◄───────────────►│                 │
│   React App     │                 │   Spring Boot   │
│   (Frontend)    │                 │   (Backend)     │
│   Port: 3000    │                 │   Port: 8080    │
└─────────────────┘                 └─────────────────┘
                                              │
                                              │ JPA/Hibernate
                                              ▼
                                    ┌─────────────────┐
                                    │   H2 Database   │
                                    │   (In Memory)   │
                                    └─────────────────┘
```

### **🔧 Backend (Spring Boot)**

#### **Estrutura de Camadas**
```
📁 com.example.projeto_test/
├── 🎮 controller/           # Controllers REST (API Layer)
│   ├── TarefaController.java
│   └── TarefaSearchController.java
├── 💼 business/             # Serviços de negócio (Business Layer)
│   └── TarefaService.java
├── 🏗️ infrastructure/       # Infraestrutura (Data Layer)
│   ├── entitys/
│   │   ├── Tarefa.java      # Entidade JPA
│   │   └── repository/
│   │       └── TarefaRepository.java
├── 📦 dto/                  # Data Transfer Objects
│   ├── TarefaDTO.java
│   └── TarefaResponseDTO.java
├── 🚨 exception/           # Tratamento de exceções
│   ├── GlobalExceptionHandler.java
│   ├── TarefaNotFoundException.java
│   └── BusinessRuleException.java
└── 🏃 ProjetoTestApplication.java
```

#### **🛠️ Tecnologias Backend**
- **Java 25** - Linguagem principal
- **Spring Boot 3.5.6** - Framework web
- **Spring Data JPA** - Persistência de dados
- **Hibernate** - ORM
- **H2 Database** - Banco em memória
- **Bean Validation** - Validação de dados
- **Lombok** - Redução de boilerplate
- **SLF4J** - Sistema de logs
- **JUnit 5** - Testes unitários
- **Mockito** - Mocks para testes

### **🎨 Frontend (React)**

#### **Estrutura Modular**
```
📁 src/
├── 🎯 components/              # Componentes React
│   ├── TaskList/              # Lista de tarefas
│   │   ├── TaskList.tsx       # Componente principal
│   │   ├── TaskList.styles.css # Estilos específicos
│   │   ├── TaskList.hooks.ts   # Lógica de estado
│   │   └── TaskList.types.ts   # Definições TypeScript
│   └── TaskForm/              # Formulário de tarefa
│       ├── TaskForm.tsx
│       ├── TaskForm.styles.css
│       ├── TaskForm.hooks.ts
│       └── TaskForm.types.ts
├── 🔧 hooks/                  # Hooks customizados
│   └── useTasks.ts           # Gerenciamento de estado global
├── 🔌 services/               # Integração com API
│   └── TaskService/
│       ├── TaskService.ts     # Serviço principal
│       ├── TaskService.api.ts # Calls HTTP
│       ├── TaskService.hooks.ts # Hooks específicos
│       └── TaskService.types.ts # Types da API
├── 🎨 styles/                # Estilos globais
│   └── Global.css           # CSS global reutilizável
├── 📝 types/                 # Definições TypeScript
│   └── Task.ts              # Interfaces das tarefas
├── 🏠 App.tsx               # Componente raiz
└── 📄 index.tsx             # Entry point
```

#### **🛠️ Tecnologias Frontend**
- **React 18.3.1** - Biblioteca UI
- **TypeScript 4.9** - Type safety
- **Axios** - Cliente HTTP
- **CSS3** - Estilização
- **React Hooks** - Gerenciamento de estado
- **Create React App** - Tooling e build

---

## 🧪 **Testes**

### **� Backend Tests**

```bash
# Executar todos os testes
cd backend
./mvnw test

# Executar testes específicos
./mvnw test -Dtest=TarefaServiceTest
./mvnw test -Dtest=TarefaControllerTest
./mvnw test -Dtest=TarefaIntegrationTest

# Relatório de cobertura
./mvnw jacoco:report
# 📊 Relatório: target/site/jacoco/index.html
```

#### **📋 Suítes de Teste**
- **Unit Tests** - Testes isolados de componentes
- **Integration Tests** - Testes end-to-end da API
- **Repository Tests** - Testes de persistência
- **Exception Tests** - Testes de cenários de erro
- **DTO Tests** - Testes de serialização/deserialização

### **🎯 Frontend Tests**

```bash
# Executar testes (quando implementados)
cd frontend
npm test

# Testes em modo watch
npm test -- --watch

# Cobertura de código
npm test -- --coverage
```

---

## 🗄️ **Banco de Dados**

### **💾 H2 Database (Desenvolvimento)**

#### **Configuração**
```properties
# application.properties
spring.datasource.url=jdbc:h2:mem:tarefasdb
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
```

#### **� Acesso ao Console H2**
1. **URL**: http://localhost:8080/h2-console
2. **JDBC URL**: `jdbc:h2:mem:tarefasdb`
3. **Username**: `sa`
4. **Password**: (deixar vazio)

#### **📊 Schema da Tabela**
```sql
CREATE TABLE tarefas (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(100) NOT NULL,
  descricao TEXT,
  status VARCHAR(20) NOT NULL,
  data_criacao TIMESTAMP NOT NULL,
  data_atualizacao TIMESTAMP
);
```

---

## 🔧 **Configuração Avançada**

### **⚙️ Variáveis de Ambiente**

#### **Backend**
```bash
# Porta do servidor
SERVER_PORT=8080

# Configuração do banco
SPRING_DATASOURCE_URL=jdbc:h2:mem:tarefasdb
SPRING_JPA_SHOW_SQL=true

# Nível de log
LOGGING_LEVEL_ROOT=INFO
```

#### **Frontend**
```bash
# URL da API
REACT_APP_API_URL=http://localhost:8080

# Ambiente
NODE_ENV=development
```

### **🐳 Docker (Futuro)**

```dockerfile
# Dockerfile para backend
FROM openjdk:25-jdk-slim
COPY target/projeto-test-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```dockerfile
# Dockerfile para frontend
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
```

---

## 📚 **Documentação Adicional**

### **📖 Guias Específicos**
- [📘 Backend README](./backend/README.md) - Detalhes da API
- [📗 Frontend README](./frontend/README.md) - Detalhes da interface
- [📙 API Documentation](./docs/api.md) - Documentação completa da API
- [📕 Development Guide](./docs/development.md) - Guia de desenvolvimento

### **🔗 Links Úteis**
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [React Documentation](https://react.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [H2 Database Documentation](http://www.h2database.com/html/main.html)

---

## 🤝 **Contribuição**

### **🚀 Como Contribuir**

1. **Fork** o repositório
2. **Crie** uma branch para sua feature (`git checkout -b feature/amazing-feature`)
3. **Commit** suas mudanças (`git commit -m 'Add amazing feature'`)
4. **Push** para a branch (`git push origin feature/amazing-feature`)
5. **Abra** um Pull Request

### **📋 Padrões de Commit**
```
feat: nova funcionalidade
fix: correção de bug
docs: documentação
style: formatação
refactor: refatoração
test: testes
chore: tarefas de manutenção
```

---

## 📄 **Licença**

Este projeto está licenciado sob a **MIT License** - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

## 👥 **Autores e Reconhecimentos**

- **Desenvolvedor Principal**: [Seu Nome]
- **Inspiração**: Demonstração de habilidades fullstack
- **Tecnologias**: Spring Boot Team, React Team

---

<div align="center">

**⭐ Se este projeto foi útil, deixe uma estrela!**

Made with ❤️ and ☕ by [Seu Nome]

</div>
```bash
# Terminal 2 - Frontend
cd frontend  
npm start

# ✅ Interface disponível em: http://localhost:3000
```

### **📋 Pré-requisitos**
- ☕ **Java 25+** (para backend)
- 🟢 **Node.js 18+** (para frontend)
- 📦 **npm 9+** (gerenciador de pacotes)

---

## 🎯 **Funcionalidades**

### **📋 Backend (API REST)**
- ✅ **CRUD Completo** de tarefas
- ✅ **Validação Robusta** com Bean Validation
- ✅ **Tratamento de Erros** com códigos HTTP apropriados
- ✅ **Banco H2** em memória para desenvolvimento
- ✅ **Testes Unitários** (80+ testes)
- ✅ **Documentação Completa** da API

### **🎨 Frontend (React)**
- ✅ **Interface Moderna** com React 18 + TypeScript
- ✅ **Componentes Reutilizáveis** 
- ✅ **Estado Global** gerenciado
- ✅ **Comunicação com API** via Axios
- ✅ **Design Responsivo** 
- ✅ **Testes de Componentes**

### **🔗 Integração**
- ✅ **CORS** configurado para desenvolvimento
- ✅ **Proxy** automático do React para API
- ✅ **Tipagem TypeScript** baseada na API
- ✅ **Tratamento de Erros** consistente

---

## 📊 **APIs Disponíveis**

### **Backend Endpoints**
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/tasks` | Lista todas as tarefas |
| `POST` | `/tasks` | Cria nova tarefa |
| `GET` | `/tasks/{id}` | Busca tarefa por ID |
| `PUT` | `/tasks/{id}` | Atualiza tarefa |
| `DELETE` | `/tasks/{id}` | Remove tarefa |

**Base URL**: `http://localhost:8080`

### **Exemplo de Uso**
```bash
# Criar tarefa
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Estudar React",
    "descricao": "Aprender hooks e context",
    "status": "PENDENTE"
  }'
```

---

## 🛠️ **Scripts de Desenvolvimento**

### **🔧 Backend (Maven)**
```bash
cd backend

# Executar aplicação
./mvnw spring-boot:run

# Executar testes
./mvnw test

# Compilar projeto
./mvnw clean compile

# Gerar JAR
./mvnw clean package
```

### **🎨 Frontend (NPM)**
```bash
cd frontend

# Executar em desenvolvimento
npm start

# Executar testes
npm test

# Build para produção
npm run build

# Análise de bundle
npm run analyze
```

### **🏠 Workspace (Raiz)**
```bash
# Instalar dependências de ambos
npm run install:all

# Executar ambos simultaneamente
npm run dev

# Executar testes de ambos
npm run test:all

# Build de produção completo
npm run build:all
```

---

## 🔧 **Configuração**

### **🌐 Configuração de CORS (Backend)**
```properties
# backend/src/main/resources/application.properties
cors.allowed.origins=http://localhost:3000
server.port=8080
```

### **📡 Configuração de Proxy (Frontend)**
```json
// frontend/package.json
{
  "proxy": "http://localhost:8080"
}
```

### **🎯 Configuração de Ambiente**
```bash
# Variáveis de ambiente do frontend
REACT_APP_API_URL=http://localhost:8080
REACT_APP_ENV=development
```

---

## 🧪 **Testes**

### **Backend (Java)**
- **JUnit 5** + **Mockito** para testes unitários
- **Spring Boot Test** para testes de integração
- **TestContainers** preparado para testes com containers

### **Frontend (React)**
- **Jest** + **React Testing Library** para componentes
- **MSW** para mock de APIs
- **Cypress** preparado para testes E2E

---

## 🚀 **Deploy e Produção**

### **🐳 Docker (Futuro)**
```yaml
# docker-compose.yml (preparado)
version: '3.8'
services:
  backend:
    build: ./backend
    ports:
      - "8080:8080"
  
  frontend:
    build: ./frontend
    ports:
      - "3000:80"
    depends_on:
      - backend
```

### **☁️ Deploy Cloud**
- **Backend**: Heroku, Railway, AWS
- **Frontend**: Vercel, Netlify, AWS S3
- **Banco**: PostgreSQL, MySQL (produção)

---

## 📚 **Tecnologias**

### **🔧 Backend Stack**
- ☕ **Java 25** - Linguagem
- 🍃 **Spring Boot 3.5.6** - Framework
- 🗄️ **H2 Database** - Banco (dev)
- 🏗️ **Maven** - Build tool
- 🧪 **JUnit 5 + Mockito** - Testes

### **🎨 Frontend Stack**
- ⚛️ **React 18** - Framework UI
- 📘 **TypeScript** - Linguagem
- 🎨 **CSS3 + Styled Components** - Estilo
- 📦 **NPM** - Gerenciador de pacotes
- 🧪 **Jest + RTL** - Testes

---

## 🤝 **Contribuição**

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Add nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

---

## 📄 **Licença**

Este projeto está sob a licença **MIT** - veja [LICENSE](LICENSE) para detalhes.

---

<div align="center">

**🚀 Desenvolvido com ❤️ usando Spring Boot + React**

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-blue.svg)](https://www.typescriptlang.org/)

</div>
