import { TarefaDTO, TarefaResponseDTO, StatusTarefa } from '../../types/Task';

export interface TaskFormProps {
  isOpen: boolean;
  task?: TarefaResponseDTO | null;
  onSave: (task: TarefaDTO) => Promise<void>;
  onCancel: () => void;
}

export interface UseTaskFormParams {
  task?: TarefaResponseDTO | null;
  onSave: (task: TarefaDTO) => Promise<void>;
  onCancel: () => void;
}

export interface TaskFormData extends TarefaDTO {
  titulo: string;
  descricao: string;
  status: StatusTarefa;
}

export interface TaskFormErrors {
  titulo?: string;
  descricao?: string;
  status?: string;
}

export interface StatusOption {
  value: StatusTarefa;
  label: string;
  icon: string;
}

export const VALIDATION_RULES = {
  titulo: {
    minLength: 3,
    maxLength: 100,
    required: 'Título é obrigatório'
  },
  descricao: {
    minLength: 5,
    maxLength: 500,
    required: 'Descrição é obrigatória'
  },
  status: {
    required: 'Status é obrigatório'
  }
} as const;

export const MAX_LENGTHS = {
  titulo: 100,
  descricao: 500
} as const;

export const STATUS_OPTIONS: readonly StatusOption[] = [
  { value: 'PENDENTE', label: 'Pendente', icon: '📋' },
  { value: 'CONCLUIDA', label: 'Concluída', icon: '✅' },
  { value: 'CANCELADA', label: 'Cancelada', icon: '❌' }
] as const;