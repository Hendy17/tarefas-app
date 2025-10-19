import { useState, useEffect, useCallback, useMemo } from 'react';
import { TarefaResponseDTO, TarefaDTO } from '../../types/Task';
import { TaskFilter, ApiError, UseTasksState } from './TaskService.types';
import { taskServiceApi } from './TaskService.api';

export const useTasks = () => {
  const [state, setState] = useState<UseTasksState>({
    tasks: [],
    loading: false,
    error: null,
    stats: { total: 0, pendentes: 0, concluidas: 0, canceladas: 0 }
  });

  const fetchTasks = useCallback(async () => {
    setState(prev => ({ ...prev, loading: true, error: null }));
    
    try {
      const tasks = await taskServiceApi.getAllTasks();
      const stats = await taskServiceApi.getTaskStats();
      
      setState(prev => ({
        ...prev,
        tasks,
        stats,
        loading: false,
        error: null
      }));
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Erro ao buscar tarefas';
      setState(prev => ({
        ...prev,
        error: errorMessage,
        loading: false
      }));
      throw error;
    }
  }, []);

  const createTask = useCallback(async (taskData: TarefaDTO): Promise<TarefaResponseDTO> => {
    setState(prev => ({ ...prev, loading: true, error: null }));
    
    try {
      const newTask = await taskServiceApi.createTask(taskData);
      setState(prev => ({
        ...prev,
        tasks: [...prev.tasks, newTask],
        loading: false
      }));
      
      await fetchTasks();
      return newTask;
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Erro ao criar tarefa';
      setState(prev => ({
        ...prev,
        error: errorMessage,
        loading: false
      }));
      throw error;
    }
  }, [fetchTasks]);

  const updateTask = useCallback(async (id: number, taskData: TarefaDTO): Promise<TarefaResponseDTO> => {
    setState(prev => ({ ...prev, loading: true, error: null }));
    
    try {
      const updatedTask = await taskServiceApi.updateTask(id, taskData);
      setState(prev => ({
        ...prev,
        tasks: prev.tasks.map(task => task.id === id ? updatedTask : task),
        loading: false
      }));
      
      await fetchTasks();
      return updatedTask;
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Erro ao atualizar tarefa';
      setState(prev => ({
        ...prev,
        error: errorMessage,
        loading: false
      }));
      throw error;
    }
  }, [fetchTasks]);

  const deleteTask = useCallback(async (id: number): Promise<void> => {
    setState(prev => ({ ...prev, loading: true, error: null }));
    
    try {
      await taskServiceApi.deleteTask(id);
      setState(prev => ({
        ...prev,
        tasks: prev.tasks.filter(task => task.id !== id),
        loading: false
      }));
      
      await fetchTasks();
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Erro ao deletar tarefa';
      setState(prev => ({
        ...prev,
        error: errorMessage,
        loading: false
      }));
      throw error;
    }
  }, [fetchTasks]);

  useEffect(() => {
    fetchTasks();
  }, [fetchTasks]);

  return {
    ...state,
    refreshTasks: fetchTasks,
    createTask,
    updateTask,
    deleteTask
  };
};

export const useTaskFilter = (tasks: TarefaResponseDTO[], filter: TaskFilter) => {
  return useMemo(() => {
    let filteredTasks = [...tasks];

    if (filter.status) {
      filteredTasks = filteredTasks.filter(task => task.status === filter.status);
    }

    if (filter.searchTerm) {
      const searchLower = filter.searchTerm.toLowerCase();
      filteredTasks = filteredTasks.filter(task =>
        task.titulo.toLowerCase().includes(searchLower) ||
        task.descricao.toLowerCase().includes(searchLower)
      );
    }

    if (filter.sortBy) {
      filteredTasks.sort((a, b) => {
        const aValue = a[filter.sortBy!];
        const bValue = b[filter.sortBy!];
        
        let comparison = 0;
        if (aValue < bValue) comparison = -1;
        if (aValue > bValue) comparison = 1;
        
        return filter.sortOrder === 'desc' ? -comparison : comparison;
      });
    }

    return filteredTasks;
  }, [tasks, filter]);
};

export const useConnectionStatus = () => {
  const [isOnline, setIsOnline] = useState(navigator.onLine);
  const [lastError, setLastError] = useState<ApiError | null>(null);

  useEffect(() => {
    const handleOnline = () => setIsOnline(true);
    const handleOffline = () => setIsOnline(false);

    window.addEventListener('online', handleOnline);
    window.addEventListener('offline', handleOffline);

    return () => {
      window.removeEventListener('online', handleOnline);
      window.removeEventListener('offline', handleOffline);
    };
  }, []);

  const testConnection = useCallback(async () => {
    try {
      await taskServiceApi.getAllTasks();
      setLastError(null);
      return true;
    } catch (error) {
      const apiError: ApiError = {
        message: error instanceof Error ? error.message : 'Erro de conexão',
        status: (error as any)?.response?.status
      };
      setLastError(apiError);
      return false;
    }
  }, []);

  return {
    isOnline,
    lastError,
    testConnection
  };
};

export const useApiMetrics = () => {
  const [metrics, setMetrics] = useState<any[]>([]);

  const addMetric = useCallback((metric: any) => {
    setMetrics(prev => [...prev.slice(-99), metric]);
  }, []);

  const getAverageResponseTime = useCallback(() => {
    if (metrics.length === 0) return 0;
    const total = metrics.reduce((sum, metric) => sum + metric.duration, 0);
    return Math.round(total / metrics.length);
  }, [metrics]);

  const getErrorRate = useCallback(() => {
    if (metrics.length === 0) return 0;
    const errors = metrics.filter(metric => metric.status >= 400).length;
    return Math.round((errors / metrics.length) * 100);
  }, [metrics]);

  return {
    metrics,
    addMetric,
    averageResponseTime: getAverageResponseTime(),
    errorRate: getErrorRate()
  };
};