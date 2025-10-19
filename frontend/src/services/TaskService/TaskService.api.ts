import axios, { AxiosResponse, AxiosError } from 'axios';
import { TarefaDTO, TarefaResponseDTO } from '../../types/Task';
import { 
  TaskService, 
  TaskStats, 
  ApiConfig, 
  ApiError, 
  ApiErrorResponse,
  API_CONSTANTS,
  RequestMetrics
} from './TaskService.types';

const apiConfig: ApiConfig = {
  baseURL: API_CONSTANTS.BASE_URL,
  timeout: API_CONSTANTS.TIMEOUT,
  headers: API_CONSTANTS.HEADERS
};

const api = axios.create({
  baseURL: apiConfig.baseURL,
  timeout: apiConfig.timeout,
  headers: apiConfig.headers,
});

let performanceMetrics: RequestMetrics[] = [];

const measurePerformance = (method: string, url: string, startTime: number, status: number) => {
  const duration = performance.now() - startTime;
  const metric: RequestMetrics = {
    method,
    url,
    duration,
    status,
    timestamp: new Date()
  };
  
  performanceMetrics.push(metric);
  
  if (performanceMetrics.length > 100) {
    performanceMetrics = performanceMetrics.slice(-100);
  }
  
  return metric;
};

api.interceptors.request.use(
  (config) => {
    (config as any).metadata = { startTime: performance.now() };
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => {
    const { method = '', url = '' } = response.config;
    const startTime = (response.config as any).metadata?.startTime || 0;
    
    measurePerformance(method.toUpperCase(), url, startTime, response.status);
    
    return response;
  },
  (error: AxiosError) => {
    const { method = '', url = '' } = error.config || {};
    const startTime = (error.config as any)?.metadata?.startTime || 0;
    const status = error.response?.status || 0;
    
    measurePerformance(method.toUpperCase(), url, startTime, status);
    
    console.error('Erro na API:', {
      message: error.message,
      status: error.response?.status,
      data: error.response?.data,
      url: error.config?.url
    });
    
    return Promise.reject(createApiError(error));
  }
);

const createApiError = (error: AxiosError): ApiError => {
  const response = error.response;
  
  if (response?.data && typeof response.data === 'object') {
    const errorData = response.data as ApiErrorResponse;
    return {
      message: errorData.message || 'Erro na API',
      status: errorData.status || response.status,
      code: errorData.error,
      details: errorData.details
    };
  }
  
  return {
    message: error.message || 'Erro de conexão',
    status: response?.status,
    code: error.code
  };
};

export const validateTaskData = (task: TarefaDTO): string[] => {
  const errors: string[] = [];
  
  if (!task.titulo?.trim()) {
    errors.push('Título é obrigatório');
  } else if (task.titulo.length > 100) {
    errors.push('Título não pode exceder 100 caracteres');
  }
  
  if (!task.descricao?.trim()) {
    errors.push('Descrição é obrigatória');
  } else if (task.descricao.length > 500) {
    errors.push('Descrição não pode exceder 500 caracteres');
  }
  
  if (!task.status) {
    errors.push('Status é obrigatório');
  } else if (!['PENDENTE', 'CONCLUIDA', 'CANCELADA'].includes(task.status)) {
    errors.push('Status inválido');
  }
  
  return errors;
};

export const transformTaskForApi = (task: TarefaDTO): TarefaDTO => {
  return {
    titulo: task.titulo.trim(),
    descricao: task.descricao.trim(),
    status: task.status
  };
};

export const transformTaskFromApi = (task: TarefaResponseDTO): TarefaResponseDTO => {
  return {
    ...task
  };
};

export const taskServiceApi: TaskService = {
  async getAllTasks(): Promise<TarefaResponseDTO[]> {
    const response: AxiosResponse<TarefaResponseDTO[]> = await api.get(apiConfig.baseURL);
    return response.data.map(transformTaskFromApi);
  },

  async getTaskById(id: number): Promise<TarefaResponseDTO> {
    if (!id || id <= 0) {
      throw new Error('ID da tarefa inválido');
    }
    
    const response: AxiosResponse<TarefaResponseDTO> = await api.get(
      `${apiConfig.baseURL}${API_CONSTANTS.ENDPOINTS.TASK_BY_ID(id)}`
    );
    return transformTaskFromApi(response.data);
  },

  async createTask(task: TarefaDTO): Promise<TarefaResponseDTO> {
    const validationErrors = validateTaskData(task);
    if (validationErrors.length > 0) {
      throw new Error(`Dados inválidos: ${validationErrors.join(', ')}`);
    }
    
    const transformedTask = transformTaskForApi(task);
    const response: AxiosResponse<TarefaResponseDTO> = await api.post(
      apiConfig.baseURL, 
      transformedTask
    );
    return transformTaskFromApi(response.data);
  },

  async updateTask(id: number, task: TarefaDTO): Promise<TarefaResponseDTO> {
    if (!id || id <= 0) {
      throw new Error('ID da tarefa inválido');
    }
    
    const validationErrors = validateTaskData(task);
    if (validationErrors.length > 0) {
      throw new Error(`Dados inválidos: ${validationErrors.join(', ')}`);
    }
    
    const transformedTask = transformTaskForApi(task);
    const response: AxiosResponse<TarefaResponseDTO> = await api.put(
      `${apiConfig.baseURL}${API_CONSTANTS.ENDPOINTS.TASK_BY_ID(id)}`, 
      transformedTask
    );
    return transformTaskFromApi(response.data);
  },

  async deleteTask(id: number): Promise<void> {
    if (!id || id <= 0) {
      throw new Error('ID da tarefa inválido');
    }
    
    await api.delete(`${apiConfig.baseURL}${API_CONSTANTS.ENDPOINTS.TASK_BY_ID(id)}`);
  },

  async getTasksByStatus(status: string): Promise<TarefaResponseDTO[]> {
    if (!status || !['PENDENTE', 'CONCLUIDA', 'CANCELADA'].includes(status)) {
      throw new Error('Status inválido');
    }
    
    const response: AxiosResponse<TarefaResponseDTO[]> = await api.get(
      `${apiConfig.baseURL}${API_CONSTANTS.ENDPOINTS.TASKS_BY_STATUS(status)}`
    );
    return response.data.map(transformTaskFromApi);
  },

  async getTaskStats(): Promise<TaskStats> {
    try {
      const [all, pending, completed, cancelled] = await Promise.all([
        this.getAllTasks(),
        this.getTasksByStatus('PENDENTE').catch(() => []),
        this.getTasksByStatus('CONCLUIDA').catch(() => []),
        this.getTasksByStatus('CANCELADA').catch(() => [])
      ]);

      return {
        total: all.length,
        pendentes: pending.length,
        concluidas: completed.length,
        canceladas: cancelled.length
      };
    } catch (error) {
      console.error('Erro ao buscar estatísticas:', error);
      throw new Error('Erro ao carregar estatísticas das tarefas');
    }
  }
};

export const getPerformanceMetrics = (): RequestMetrics[] => {
  return [...performanceMetrics];
};

export const getAverageResponseTime = (): number => {
  if (performanceMetrics.length === 0) return 0;
  const total = performanceMetrics.reduce((sum, metric) => sum + metric.duration, 0);
  return Math.round(total / performanceMetrics.length);
};

export const getErrorRate = (): number => {
  if (performanceMetrics.length === 0) return 0;
  const errors = performanceMetrics.filter(metric => metric.status >= 400).length;
  return Math.round((errors / performanceMetrics.length) * 100);
};

export const clearPerformanceMetrics = (): void => {
  performanceMetrics = [];
};

export const testApiConnection = async (): Promise<boolean> => {
  try {
    await api.get(apiConfig.baseURL, { timeout: 5000 });
    return true;
  } catch (error) {
    console.warn('Teste de conectividade falhou:', error);
    return false;
  }
};

export default taskServiceApi;