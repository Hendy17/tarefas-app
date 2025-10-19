import { TarefaResponseDTO, TarefaDTO, StatusTarefa } from '../types/Task';

export interface UseTasksResult {
  tasks: TarefaResponseDTO[];
  loading: boolean;
  error: string | null;
  stats: { total: number; pendentes: number; concluidas: number };
  

  createTask: (task: TarefaDTO) => Promise<TarefaResponseDTO | null>;
  updateTask: (id: number, task: TarefaDTO) => Promise<TarefaResponseDTO | null>;
  deleteTask: (id: number) => Promise<boolean>;
  refreshTasks: () => Promise<void>;
  
  
  filterByStatus: (status: StatusTarefa | null) => void;
  searchTasks: (searchTerm: string) => void;
}