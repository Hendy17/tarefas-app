import { useState, useEffect, useCallback } from 'react';
import { TarefaResponseDTO, TarefaDTO, StatusTarefa } from '../types/Task';
import taskService from '../services/TaskService/TaskService';
import { UseTasksResult } from './useTasks.types';

export const useTasks = (): UseTasksResult => {
  const [tasks, setTasks] = useState<TarefaResponseDTO[]>([]);
  const [allTasks, setAllTasks] = useState<TarefaResponseDTO[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [stats, setStats] = useState({ total: 0, pendentes: 0, concluidas: 0 });

  const refreshTasks = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      
      const [tasksData, statsData] = await Promise.all([
        taskService.getAllTasks(),
        taskService.getTaskStats()
      ]);
      
      setTasks(tasksData);
      setAllTasks(tasksData);
      setStats(statsData);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Erro ao carregar tarefas');
      console.error('Erro ao carregar tarefas:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  const createTask = useCallback(async (task: TarefaDTO): Promise<TarefaResponseDTO | null> => {
    try {
      setError(null);
      const newTask = await taskService.createTask(task);
      await refreshTasks(); 
      return newTask;
    } catch (err: any) {
      setError(err.response?.data?.message || 'Erro ao criar tarefa');
      console.error('Erro ao criar tarefa:', err);
      return null;
    }
  }, [refreshTasks]);

  const updateTask = useCallback(async (id: number, task: TarefaDTO): Promise<TarefaResponseDTO | null> => {
    try {
      setError(null);
      const updatedTask = await taskService.updateTask(id, task);
      await refreshTasks(); 
      return updatedTask;
    } catch (err: any) {
      setError(err.response?.data?.message || 'Erro ao atualizar tarefa');
      console.error('Erro ao atualizar tarefa:', err);
      return null;
    }
  }, [refreshTasks]);

  const deleteTask = useCallback(async (id: number): Promise<boolean> => {
    try {
      setError(null);
      await taskService.deleteTask(id);
      await refreshTasks(); 
      return true;
    } catch (err: any) {
      setError(err.response?.data?.message || 'Erro ao deletar tarefa');
      console.error('Erro ao deletar tarefa:', err);
      return false;
    }
  }, [refreshTasks]);

  const filterByStatus = useCallback((status: StatusTarefa | null) => {
    if (status === null) {
      setTasks(allTasks);
    } else {
      const filteredTasks = allTasks.filter(task => task.status === status);
      setTasks(filteredTasks);
    }
  }, [allTasks]);

  const searchTasks = useCallback((searchTerm: string) => {
    if (!searchTerm.trim()) {
      setTasks(allTasks);
    } else {
      const filtered = allTasks.filter(task => 
        task.titulo.toLowerCase().includes(searchTerm.toLowerCase()) ||
        task.descricao.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setTasks(filtered);
    }
  }, [allTasks]);

  
  useEffect(() => {
    refreshTasks();
  }, [refreshTasks]);

  return {
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
  };
};