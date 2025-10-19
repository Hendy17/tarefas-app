import { useState, useCallback, useMemo } from 'react';
import { TarefaResponseDTO, StatusTarefa } from '../../types/Task';
import { 
  TaskListProps, 
  LoadingState, 
  TaskStats,
  STATUS_CONFIG,
  TEXT_LIMITS,
  DATE_FORMAT_OPTIONS
} from './TaskList.types';

export const useTaskList = (props: TaskListProps) => {
  const { tasks, onEdit, onDelete, onStatusChange } = props;
  
  const [loadingStates, setLoadingStates] = useState<Record<number, LoadingState>>({});
  const [selectedTasks, setSelectedTasks] = useState<Set<number>>(new Set());
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [actionPending, setActionPending] = useState<{
    type: 'delete' | 'status-change';
    taskId: number;
    newStatus?: StatusTarefa;
  } | null>(null);

  const stats: TaskStats = useMemo(() => {
    return {
      total: tasks.length,
      pendentes: tasks.filter(task => task.status === 'PENDENTE').length,
      concluidas: tasks.filter(task => task.status === 'CONCLUIDA').length,
      canceladas: tasks.filter(task => task.status === 'CANCELADA').length
    };
  }, [tasks]);

  const hasTasks = tasks.length > 0;
  const hasSelectedTasks = selectedTasks.size > 0;

  const handleEdit = useCallback((task: TarefaResponseDTO) => {
    onEdit(task);
  }, [onEdit]);

  const handleDelete = useCallback((taskId: number) => {
    setActionPending({ type: 'delete', taskId });
    setShowConfirmModal(true);
  }, []);

  const handleStatusChange = useCallback((taskId: number, newStatus: StatusTarefa) => {
    const task = tasks.find(t => t.id === taskId);
    if (task && task.status !== newStatus) {
      setActionPending({ type: 'status-change', taskId, newStatus });
      setShowConfirmModal(true);
    }
  }, [tasks]);

  const confirmAction = useCallback(async () => {
    if (!actionPending) return;

    const { type, taskId, newStatus } = actionPending;
    
    setLoadingStates(prev => ({ ...prev, [taskId]: type === 'delete' ? 'deleting' : 'updating' }));
    
    try {
      if (type === 'delete') {
        await onDelete(taskId);
      } else if (type === 'status-change' && newStatus) {
        await onStatusChange(taskId, newStatus);
      }
    } catch (error) {
      console.error('Erro ao executar ação:', error);
    } finally {
      setLoadingStates(prev => ({ ...prev, [taskId]: 'idle' }));
      setShowConfirmModal(false);
      setActionPending(null);
    }
  }, [actionPending, onDelete, onStatusChange]);

  const cancelAction = useCallback(() => {
    setShowConfirmModal(false);
    setActionPending(null);
  }, []);

  const toggleTaskSelection = useCallback((taskId: number) => {
    setSelectedTasks(prev => {
      const newSet = new Set(prev);
      if (newSet.has(taskId)) {
        newSet.delete(taskId);
      } else {
        newSet.add(taskId);
      }
      return newSet;
    });
  }, []);

  const selectAllTasks = useCallback(() => {
    setSelectedTasks(new Set(tasks.map(task => task.id)));
  }, [tasks]);

  const clearSelection = useCallback(() => {
    setSelectedTasks(new Set());
  }, []);

  const deleteSelectedTasks = useCallback(() => {
    if (hasSelectedTasks) {
      selectedTasks.forEach(taskId => handleDelete(taskId));
    }
  }, [selectedTasks, hasSelectedTasks, handleDelete]);

  return {
    loadingStates,
    selectedTasks,
    showConfirmModal,
    actionPending,
    stats,
    hasTasks,
    hasSelectedTasks,
    
    handleEdit,
    handleDelete,
    handleStatusChange,
    confirmAction,
    cancelAction,
    toggleTaskSelection,
    selectAllTasks,
    clearSelection,
    deleteSelectedTasks
  };
};

export const useTextFormatter = () => {
  const truncateText = useCallback((text: string, maxLength: number): string => {
    if (text.length <= maxLength) return text;
    return text.substring(0, maxLength).trim() + '...';
  }, []);

  const formatTitlePreview = useCallback((titulo: string): string => {
    return truncateText(titulo, TEXT_LIMITS.titulo.preview);
  }, [truncateText]);

  const formatDescriptionPreview = useCallback((descricao: string): string => {
    return truncateText(descricao, TEXT_LIMITS.descricao.preview);
  }, [truncateText]);

  const formatDate = useCallback((dateString: string): string => {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR', DATE_FORMAT_OPTIONS);
  }, []);

  const getRelativeTime = useCallback((dateString: string): string => {
    const now = new Date();
    const date = new Date(dateString);
    const diffMs = now.getTime() - date.getTime();
    const diffMinutes = Math.floor(diffMs / (1000 * 60));
    const diffHours = Math.floor(diffMinutes / 60);
    const diffDays = Math.floor(diffHours / 24);

    if (diffMinutes < 1) return 'Agora mesmo';
    if (diffMinutes < 60) return `há ${diffMinutes} min`;
    if (diffHours < 24) return `há ${diffHours}h`;
    if (diffDays < 7) return `há ${diffDays} dias`;
    
    return formatDate(dateString);
  }, [formatDate]);

  return {
    truncateText,
    formatTitlePreview,
    formatDescriptionPreview,
    formatDate,
    getRelativeTime
  };
};

export const useTaskAnimations = () => {
  const [animatingTasks, setAnimatingTasks] = useState<Set<number>>(new Set());

  const animateTaskEntry = useCallback((taskId: number) => {
    setAnimatingTasks(prev => new Set(prev).add(taskId));
    setTimeout(() => {
      setAnimatingTasks(prev => {
        const newSet = new Set(prev);
        newSet.delete(taskId);
        return newSet;
      });
    }, 300);
  }, []);

  const animateTaskExit = useCallback((taskId: number, onComplete: () => void) => {
    setAnimatingTasks(prev => new Set(prev).add(taskId));
    setTimeout(() => {
      onComplete();
      setAnimatingTasks(prev => {
        const newSet = new Set(prev);
        newSet.delete(taskId);
        return newSet;
      });
    }, 300);
  }, []);

  const isTaskAnimating = useCallback((taskId: number): boolean => {
    return animatingTasks.has(taskId);
  }, [animatingTasks]);

  return {
    animateTaskEntry,
    animateTaskExit,
    isTaskAnimating
  };
};

export const useStatusConfig = () => {
  const getStatusConfig = useCallback((status: StatusTarefa) => {
    return STATUS_CONFIG[status];
  }, []);

  const getStatusClass = useCallback((status: StatusTarefa): string => {
    return `status-${status.toLowerCase()}`;
  }, []);

  const isFinalStatus = useCallback((status: StatusTarefa): boolean => {
    return status === 'CONCLUIDA' || status === 'CANCELADA';
  }, []);

  return {
    getStatusConfig,
    getStatusClass,
    isFinalStatus,
    STATUS_CONFIG
  };
};