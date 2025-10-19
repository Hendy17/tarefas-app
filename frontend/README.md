# 🎨 **Frontend - Interface React Tarefas**

Interface moderna para o sistema de gerenciamento de tarefas desenvolvida com React e TypeScript.

---

## 🎯 **Visão Geral**

Interface **React 18.3.1** com **TypeScript** oferecendo:
- ✅ Interface moderna e responsiva
- ✅ Arquitetura modular com componentes reutilizáveis
- ✅ Integração completa com API backend
- ✅ Gerenciamento de estado com hooks customizados
- ✅ Validação de formulários
- ✅ Design system consistente
- ✅ TypeScript para type safety

---

## 🚀 **Início Rápido**

### **Pré-requisitos**
```bash
# Node.js 18+ e npm
node --version
npm --version
```

### **Execução**
```bash
# Instalar dependências
npm install

# Executar em modo desenvolvimento
npm start

# App disponível em: http://localhost:3000
```

### **Build**
```bash
# Build para produção
npm run build

# Build gerado em: build/
```

---

## 🏗️ **Arquitetura Modular**

### **Estrutura de Componentes**
```
📁 src/
├── 🎯 components/              # Componentes React
│   ├── TaskList/              # Lista de tarefas (4 arquivos)
│   └── TaskForm/              # Formulário (4 arquivos)
├── 🔧 hooks/                  # Hooks globais
├── 🔌 services/               # Integração com API (4 arquivos)
├── 🎨 styles/                # Estilos globais
├── 📝 types/                 # TypeScript types
├── 🏠 App.tsx               # Componente raiz
└── 📄 index.tsx             # Entry point
```

---

## 🔧 **Funcionalidades**

### **Gerenciamento de Tarefas**
- ✅ **Listar** todas as tarefas
- ✅ **Criar** nova tarefa
- ✅ **Editar** tarefa existente
- ✅ **Excluir** tarefa
- ✅ **Alterar status** (Pendente → Concluída → Cancelada)

### **Interface do Usuário**
- ✅ **Design responsivo** (mobile-first)
- ✅ **Feedback visual** para ações
- ✅ **Estados de loading** durante requisições
- ✅ **Tratamento de erros** com mensagens claras
- ✅ **Validação** de formulários em tempo real

---

## 🔌 **Integração com API**

### **Configuração**
```typescript
// TaskService.api.ts
const BASE_URL = 'http://localhost:8080';
```

### **Hooks Customizados**
```typescript
// useTasks.ts - Hook principal
const {
  tasks,          // Lista de tarefas
  loading,        // Estado de carregamento
  error,          // Mensagens de erro
  createTask,     // Criar tarefa
  updateTask,     // Atualizar tarefa
  deleteTask,     // Deletar tarefa
  refreshTasks,   // Recarregar lista
} = useTasks();
```

---

## 🌟 **Tecnologias Utilizadas**

| Tecnologia | Versão | Descrição |
|-----------|--------|-----------|
| **React** | 18.3.1 | Biblioteca UI |
| **TypeScript** | 4.9 | Type safety |
| **Axios** | latest | Cliente HTTP |
| **CSS3** | - | Estilização |
| **Create React App** | - | Tooling e build |

---

## 🔄 **Scripts Disponíveis**

```bash
npm start              # Servidor de desenvolvimento
npm run build          # Build para produção
npm test               # Executa testes
npm run eject          # Remove CRA (irreversível)
```

---

## 🚀 **Deploy**

### **Build para Produção**
```bash
# Gerar build otimizado
npm run build

# Servir estaticamente
npx serve -s build
```

### **Variáveis de Ambiente**
```bash
# .env
REACT_APP_API_URL=http://localhost:8080
NODE_ENV=production
```

---

**🎨 Frontend desenvolvido com ❤️ usando React + TypeScript**

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn’t feel obligated to use this feature. However we understand that this tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).
