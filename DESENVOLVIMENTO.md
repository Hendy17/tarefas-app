# 🚀 **Tarefas App - Guia de Desenvolvimento**

## 📋 **Configuração Inicial**

### **1. Instalar Dependências**
```bash
# Na raiz do projeto
npm install

# Instalar todas as dependências (backend + frontend)
npm run install:all
```

### **2. Executar Aplicação**
```bash
# Executar backend e frontend simultaneamente
npm run dev

# OU executar separadamente:

# Terminal 1 - Backend
npm run dev:backend
# ✅ API: http://localhost:8080

# Terminal 2 - Frontend  
npm run dev:frontend
# ✅ Interface: http://localhost:3000
```

---

## 🎯 **Scripts Disponíveis**

### **🔄 Desenvolvimento**
```bash
# Executar ambos (backend + frontend)
npm run dev

# Executar apenas backend
npm run dev:backend

# Executar apenas frontend
npm run dev:frontend
```

### **🧪 Testes**
```bash
# Executar todos os testes
npm run test:all

# Testes do backend
npm run test:backend

# Testes do frontend
npm run test:frontend
```

### **🏗️ Build**
```bash
# Build completo (backend + frontend)
npm run build:all

# Build apenas backend
npm run build:backend

# Build apenas frontend
npm run build:frontend
```

### **🧹 Limpeza**
```bash
# Limpar tudo
npm run clean:all

# Limpar apenas backend
npm run clean:backend

# Limpar apenas frontend
npm run clean:frontend
```

### **🐳 Docker**
```bash
# Build das imagens
npm run docker:build

# Subir containers
npm run docker:up

# Parar containers
npm run docker:down
```

---

## 🔧 **Desenvolvimento Frontend**

### **Próximos Passos**
1. **Configurar Axios** para comunicação com API
2. **Criar Componentes** para listagem de tarefas
3. **Implementar Formulários** para CRUD
4. **Adicionar Roteamento** com React Router
5. **Configurar Estado Global** (Context/Redux)

### **Estrutura Sugerida**
```
frontend/src/
├── components/        # Componentes reutilizáveis
│   ├── TaskList/     
│   ├── TaskForm/     
│   └── TaskItem/     
├── pages/            # Páginas da aplicação
│   ├── Home/         
│   ├── Tasks/        
│   └── About/        
├── services/         # Comunicação com API
│   └── taskApi.ts    
├── types/            # Tipos TypeScript
│   └── Task.ts       
├── hooks/            # Custom hooks
│   └── useTasks.ts   
└── utils/            # Utilitários
    └── constants.ts  
```

---

## 🌐 **Configuração da API**

### **CORS (Backend)**
O backend já está configurado para aceitar requisições do frontend:
```properties
# backend/src/main/resources/application.properties
cors.allowed.origins=http://localhost:3000
```

### **Proxy (Frontend)**
O frontend já está configurado para redirecionar requisições para a API:
```json
// frontend/package.json
{
  "proxy": "http://localhost:8080"
}
```

---

## 📊 **Status do Projeto**

### ✅ **Concluído**
- [x] Backend completo com Spring Boot
- [x] API REST com 11 endpoints
- [x] Validação e tratamento de erros
- [x] Testes unitários (80+ testes)
- [x] Documentação completa
- [x] Frontend inicializado com React + TypeScript
- [x] Estrutura de projeto organizada
- [x] Scripts de desenvolvimento

### 🚧 **Em Desenvolvimento**
- [ ] Componentes React para interface
- [ ] Integração Frontend ↔ Backend
- [ ] Testes de componentes
- [ ] Estilização com CSS/Styled Components

### 🎯 **Próximas Etapas**
- [ ] Configurar Docker
- [ ] Deploy para produção
- [ ] Banco de dados em produção
- [ ] Testes E2E com Cypress

---

## 🆘 **Troubleshooting**

### **Backend não inicia**
```bash
# Verificar versão do Java
java --version

# Deve ser Java 25+
# Se não tiver, instalar OpenJDK 25
```

### **Frontend não inicia**
```bash
# Verificar versão do Node
node --version
npm --version

# Deve ser Node 18+ e NPM 9+
# Limpar cache se necessário
npm cache clean --force
```

### **Erro de CORS**
```bash
# Verificar se backend está rodando na porta 8080
# Verificar se frontend está rodando na porta 3000
# Conferir configuração do proxy no package.json
```

---

## 📚 **Recursos**

- [Documentação da API Backend](backend/README.md)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [React Docs](https://reactjs.org/docs)
- [TypeScript Docs](https://www.typescriptlang.org/docs)

---

**🎉 Agora você está pronto para desenvolver! Execute `npm run dev` e comece a codificar! 🚀**