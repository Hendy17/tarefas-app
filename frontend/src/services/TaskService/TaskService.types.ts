import { TarefaDTO, TarefaResponseDTO } from '../../types/Task';

export interface TaskStats {
  total: number;
  pendentes: number;
  concluidas: number;
  canceladas: number;
}

export interface UseTasksState {
  tasks: TarefaResponseDTO[];
  loading: boolean;
  error: string | null;
  stats: TaskStats;
}

export interface TaskService {
  getAllTasks(): Promise<TarefaResponseDTO[]>;
  getTaskById(id: number): Promise<TarefaResponseDTO>;
  createTask(task: TarefaDTO): Promise<TarefaResponseDTO>;
  updateTask(id: number, task: TarefaDTO): Promise<TarefaResponseDTO>;
  deleteTask(id: number): Promise<void>;
  getTasksByStatus(status: string): Promise<TarefaResponseDTO[]>;
  getTaskStats(): Promise<TaskStats>;
}

export interface ApiConfig {
  baseURL: string;
  timeout: number;
  headers: Record<string, string>;
}

export interface ApiError {
  message: string;
  status?: number;
  code?: string;
  details?: unknown;
}

export interface ApiErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  details?: Record<string, unknown>;
}

export type TaskFilter = {
  status?: string;
  searchTerm?: string;
  sortBy?: 'titulo' | 'dataCriacao' | 'dataAtualizacao';
  sortOrder?: 'asc' | 'desc';
};

export const API_CONSTANTS = {
  BASE_URL: 'http://localhost:8080/tasks',
  ENDPOINTS: {
    TASKS: '',
    TASK_BY_ID: (id: number) => `/${id}`,
    TASKS_BY_STATUS: (status: string) => `/status/${status}`,
    TASK_STATS: '/stats'
  },
  TIMEOUT: 10000,
  HEADERS: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
} as const;

export enum HttpStatus {
  OK = 200,
  CREATED = 201,
  NO_CONTENT = 204,
  BAD_REQUEST = 400,
  UNAUTHORIZED = 401,
  FORBIDDEN = 403,
  NOT_FOUND = 404,
  INTERNAL_SERVER_ERROR = 500
}

export interface RequestMetrics {
  method: string;
  url: string;
  duration: number;
  status: number;
  timestamp: Date;
}