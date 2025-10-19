import React from 'react';
import { TaskListProps } from './TaskList.types';
import { 
  useTaskList, 
  useTextFormatter, 
  useStatusConfig 
} from './TaskList.hooks';
import './TaskList.styles.css';


const ConfirmModal: React.FC<{
  isOpen: boolean;
  title: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
  type?: 'danger' | 'warning' | 'info';
}> = ({ isOpen, title, message, onConfirm, onCancel, type = 'danger' }) => {
  if (!isOpen) return null;

  return (
    <div className="confirm-modal-overlay" onClick={onCancel}>
      <div className="confirm-modal" onClick={(e) => e.stopPropagation()}>
        <h3 className="confirm-modal-title">
          {type === 'danger' ? '⚠️' : type === 'warning' ? '⚡' : 'ℹ️'}
          {title}
        </h3>
        <p className="confirm-modal-message">{message}</p>
        <div className="confirm-modal-actions">
          <button 
            className="confirm-button cancel" 
            onClick={onCancel}
          >
            Cancelar
          </button>
          <button 
            className={`confirm-button ${type}`} 
            onClick={onConfirm}
          >
            Confirmar
          </button>
        </div>
      </div>
    </div>
  );
};

const TaskItem: React.FC<{
  task: any;
  onEdit: () => void;
  onDelete: () => void;
  onStatusChange: (status: any) => void;
  isLoading: boolean;
}> = ({ task, onEdit, onDelete, onStatusChange, isLoading }) => {
  const { getStatusConfig, getStatusClass } = useStatusConfig();
  const { formatTitlePreview, formatDescriptionPreview, getRelativeTime } = useTextFormatter();
  
  const statusConfig = getStatusConfig(task.status);
  const statusClass = getStatusClass(task.status);

  const handleStatusCycle = () => {
    const statusOrder = ['PENDENTE', 'CONCLUIDA', 'CANCELADA'];
    const currentIndex = statusOrder.indexOf(task.status);
    const nextIndex = (currentIndex + 1) % statusOrder.length;
    const nextStatus = statusOrder[nextIndex];
    onStatusChange(nextStatus);
  };

  return (
    <div className={`task-item ${statusClass} ${isLoading ? 'loading' : ''}`}>
      <div className="task-header">
        <h3 className="task-title" title={task.titulo}>
          {formatTitlePreview(task.titulo)}
        </h3>
        <div className={`task-status ${statusClass}`}>
          <span>{statusConfig.icon}</span>
          <span>{statusConfig.label}</span>
        </div>
      </div>

      <div className="task-description" title={task.descricao}>
        {formatDescriptionPreview(task.descricao)}
      </div>

      <div className="task-meta">
        <div className="task-date">
          {getRelativeTime(task.dataAtualizacao || task.dataCriacao)}
        </div>
        
        <div className="task-actions">
          <button
            className="action-button status-change"
            onClick={handleStatusCycle}
            disabled={isLoading}
            title="Alterar status"
          >
            🔄
          </button>
          
          <button
            className="action-button edit"
            onClick={(e) => {
              e.preventDefault();
              e.stopPropagation();
              onEdit();
            }}
            disabled={isLoading}
            title="Editar tarefa"
          >
            ✏️
          </button>
          
          <button
            className="action-button delete"
            onClick={(e) => {
              e.preventDefault();
              e.stopPropagation();
              onDelete();
            }}
            disabled={isLoading}
            title="Excluir tarefa"
          >
            🗑️
          </button>
        </div>
      </div>
    </div>
  );
};

const TaskStats: React.FC<{ stats: any }> = ({ stats }) => (
  <div className="task-stats">
    <div className="stat-card stat-total">
      <span className="stat-number">{stats.total}</span>
      <span className="stat-label">Total</span>
    </div>
    <div className="stat-card stat-pendente">
      <span className="stat-number">{stats.pendentes}</span>
      <span className="stat-label">Pendentes</span>
    </div>
    <div className="stat-card stat-concluida">
      <span className="stat-number">{stats.concluidas}</span>
      <span className="stat-label">Concluídas</span>
    </div>
    <div className="stat-card stat-cancelada">
      <span className="stat-number">{stats.canceladas}</span>
      <span className="stat-label">Canceladas</span>
    </div>
  </div>
);

export const TaskList: React.FC<TaskListProps> = (props) => {
  const { tasks, loading, error } = props;
  
  const {
    loadingStates,
    showConfirmModal,
    actionPending,
    stats,
    hasTasks,
    handleEdit,
    handleDelete,
    handleStatusChange,
    confirmAction,
    cancelAction
  } = useTaskList(props);

  if (loading) {
    return (
      <div className="task-list-container">
        <div className="loading-state">
          <div className="loading-spinner" />
          <div className="loading-text">Carregando tarefas...</div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="task-list-container">
        <div className="error-state">
          Erro ao carregar tarefas: {error}
        </div>
      </div>
    );
  }

  if (!hasTasks) {
    return (
      <div className="task-list-container">
        <div className="empty-state">
          <span className="empty-state-icon">📝</span>
          <h3 className="empty-state-title">Nenhuma tarefa encontrada</h3>
          <p className="empty-state-message">
            Crie sua primeira tarefa ou ajuste os filtros de busca.
          </p>
        </div>
      </div>
    );
  }

  const getConfirmMessage = () => {
    if (!actionPending) return '';
    
    const task = tasks.find(t => t.id === actionPending.taskId);
    if (!task) return '';

    if (actionPending.type === 'delete') {
      return `Tem certeza que deseja excluir a tarefa "${task.titulo}"? Esta ação não pode ser desfeita.`;
    } else if (actionPending.type === 'status-change' && actionPending.newStatus) {
      const statusLabels = {
        PENDENTE: 'Pendente',
        CONCLUIDA: 'Concluída',
        CANCELADA: 'Cancelada'
      };
      const newStatusLabel = statusLabels[actionPending.newStatus];
      return `Alterar status da tarefa "${task.titulo}" para "${newStatusLabel}"?`;
    }
    
    return '';
  };

  return (
    <div className="task-list-container">
      
      <TaskStats stats={stats} />

      
      <div className="task-list" role="list">
        {tasks.map((task) => (
          <TaskItem
            key={task.id}
            task={task}
            onEdit={() => handleEdit(task)}
            onDelete={() => handleDelete(task.id!)}
            onStatusChange={(status) => handleStatusChange(task.id!, status)}
            isLoading={(loadingStates[task.id!] || 'idle') !== 'idle'}
          />
        ))}
      </div>

      
      <ConfirmModal
        isOpen={showConfirmModal}
        title={actionPending?.type === 'delete' ? 'Excluir Tarefa' : 'Alterar Status'}
        message={getConfirmMessage()}
        onConfirm={confirmAction}
        onCancel={cancelAction}
        type={actionPending?.type === 'delete' ? 'danger' : 'info'}
      />
    </div>
  );
};

export default TaskList;