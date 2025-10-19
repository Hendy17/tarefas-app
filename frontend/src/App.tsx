import React, { useState, useEffect } from 'react';
import './App.css';
import { useTasks } from './hooks/useTasks';
import TaskList from './components/TaskList/TaskList';
import TaskForm from './components/TaskForm/TaskForm';
import { TarefaResponseDTO, TarefaDTO, StatusTarefa } from './types/Task';

function App() {
  const {
    tasks,
    loading,
    error,
    stats,
    createTask,
    updateTask,
    deleteTask,
    refreshTasks,
    filterByStatus,
    searchTasks
  } = useTasks();

  const [showForm, setShowForm] = useState(false);
  const [editingTask, setEditingTask] = useState<TarefaResponseDTO | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [activeFilter, setActiveFilter] = useState<StatusTarefa | null>(null);
  const [connectionStatus, setConnectionStatus] = useState<'loading' | 'connected' | 'disconnected'>('loading');

  useEffect(() => {
    const checkConnection = () => {
      if (loading) {
        setConnectionStatus('loading');
      } else if (error) {
        setConnectionStatus('disconnected');
      } else {
        setConnectionStatus('connected');
      }
    };

    checkConnection();
  }, [loading, error]);

  const handleCreateTask = () => {
    setEditingTask(null);
    setShowForm(true);
  };

  const handleEditTask = (task: TarefaResponseDTO) => {
    setEditingTask(task);
    setShowForm(true);
  };  const handleFormSubmit = async (taskData: TarefaDTO) => {
    try {
      if (editingTask) {
        await updateTask(editingTask.id!, taskData);
      } else {
        await createTask(taskData);
      }
      setShowForm(false);
      setEditingTask(null);
    } catch (error) {
      console.error('Erro ao salvar tarefa:', error);
    }
  };

  const handleFormCancel = () => {
    setShowForm(false);
    setEditingTask(null);
  };

  const handleDeleteTask = async (id: number) => {
    await deleteTask(id);
  };

  const handleStatusChange = async (id: number, newStatus: StatusTarefa) => {
    const task = tasks.find(t => t.id === id);
    if (task) {
      await updateTask(id, {
        titulo: task.titulo,
        descricao: task.descricao,
        status: newStatus
      });
    }
  };

  const handleSearch = (term: string) => {
    setSearchTerm(term);
    searchTasks(term);
  };

  const handleFilter = (status: StatusTarefa | null) => {
    setActiveFilter(status);
    filterByStatus(status);
    if (searchTerm) {
      setTimeout(() => searchTasks(searchTerm), 0);
    }
  };


  return (
    <div className="App">
      <header className="app-header">
        <h1 className="app-title">🚀 Tarefas App</h1>
        <p className="app-subtitle">Sistema de Gerenciamento de Tarefas</p>
      </header>

      {connectionStatus === 'connected' && (
        <div className="stats-container">
          <div className="stat-card">
            <p className="stat-number">{stats.total}</p>
            <p className="stat-label">Total de Tarefas</p>
          </div>
          <div className="stat-card">
            <p className="stat-number">{stats.pendentes}</p>
            <p className="stat-label">Pendentes</p>
          </div>
          <div className="stat-card">
            <p className="stat-number">{stats.concluidas}</p>
            <p className="stat-label">Concluídas</p>
          </div>
        </div>
      )}

      <div className="controls-container">
        <div className="search-container">
          <input
            type="text"
            className="search-input"
            placeholder="🔍 Buscar tarefas por título ou descrição..."
            value={searchTerm}
            onChange={(e) => handleSearch(e.target.value)}
            disabled={loading}
          />
        </div>

        <div className="filters-container">
          <button
            className={`filter-button ${activeFilter === null ? 'active' : ''}`}
            onClick={() => handleFilter(null)}
            disabled={loading}
          >
            Todas
          </button>
          <button
            className={`filter-button ${activeFilter === 'PENDENTE' ? 'active' : ''}`}
            onClick={() => handleFilter('PENDENTE')}
            disabled={loading}
          >
            📋 Pendentes
          </button>
          <button
            className={`filter-button ${activeFilter === 'CONCLUIDA' ? 'active' : ''}`}
            onClick={() => handleFilter('CONCLUIDA')}
            disabled={loading}
          >
            ✅ Concluídas
          </button>
          <button
            className={`filter-button ${activeFilter === 'CANCELADA' ? 'active' : ''}`}
            onClick={() => handleFilter('CANCELADA')}
            disabled={loading}
          >
            ❌ Canceladas
          </button>
        </div>

        <button
          className="refresh-button"
          onClick={refreshTasks}
          disabled={loading}
        >
          {loading ? '🔄' : '🔄'} 
          {loading ? 'Carregando...' : 'Atualizar'}
        </button>
      </div>

      {connectionStatus === 'connected' && (
        <div style={{ textAlign: 'center', margin: '2rem 0' }}>
          <button
            className="refresh-button"
            onClick={handleCreateTask}
            style={{ background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }}
          >
            ➕ Nova Tarefa
          </button>
        </div>
      )}

      {loading && (
        <div className="loading-message">
          <div>🔄 Carregando tarefas...</div>
        </div>
      )}

      {error && (
        <div className="error-message">
          <strong>❌ Erro:</strong> {error}
          <br />
          <small>Certifique-se de que o backend está rodando em http://localhost:8080</small>
        </div>
      )}

      {connectionStatus === 'connected' && (
        <TaskList
          tasks={tasks}
          loading={loading}
          error={error}
          onEdit={handleEditTask}
          onDelete={handleDeleteTask}
          onStatusChange={handleStatusChange}
        />
      )}

      {connectionStatus === 'disconnected' && (
        <div className="empty-message">
          <div className="empty-message-icon">⚠️</div>
          <h3>Backend não está disponível</h3>
          <p>Para usar a aplicação, execute o backend:</p>
          <div className="code-block">
            <div>cd backend</div>
            <div>./mvnw spring-boot:run</div>
          </div>
          <p>Depois clique em "Atualizar" para reconectar.</p>
        </div>
      )}

      <TaskForm
        task={editingTask}
        onSave={handleFormSubmit}
        onCancel={handleFormCancel}
        isOpen={showForm}
      />
    </div>
  );
}

export default App;
