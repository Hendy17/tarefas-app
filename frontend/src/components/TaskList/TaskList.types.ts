import { TarefaResponseDTO, StatusTarefa } from '../../types/Task';

export interface TaskListProps {
  tasks: TarefaResponseDTO[];
  loading: boolean;
  error: string | null;
  onEdit: (task: TarefaResponseDTO) => void;
  onDelete: (id: number) => void;
  onStatusChange: (id: number, status: StatusTarefa) => void;
}

export interface TaskItemProps {
  task: TarefaResponseDTO;
  onEdit: (task: TarefaResponseDTO) => void;
  onDelete: (id: number) => void;
  onStatusChange: (id: number, status: StatusTarefa) => void;
}

export interface TaskActionsProps {
  task: TarefaResponseDTO;
  onEdit: () => void;
  onDelete: () => void;
  onStatusChange: (status: StatusTarefa) => void;
}

export interface ConfirmModalProps {
  isOpen: boolean;
  title: string;
  message: string;
  confirmText: string;
  cancelText: string;
  onConfirm: () => void;
  onCancel: () => void;
  type?: 'danger' | 'warning' | 'info';
}

export type LoadingState = 'idle' | 'loading' | 'deleting' | 'updating';

export interface TaskStats {
  total: number;
  pendentes: number;
  concluidas: number;
  canceladas: number;
}

export const STATUS_CONFIG = {
  PENDENTE: {
    label: 'Pendente',
    icon: '📋',
    color: '#ffc107',
    bgColor: '#fff3cd',
    borderColor: '#ffeaa7'
  },
  CONCLUIDA: {
    label: 'Concluída',
    icon: '✅',
    color: '#28a745',
    bgColor: '#d4edda',
    borderColor: '#c3e6cb'
  },
  CANCELADA: {
    label: 'Cancelada',
    icon: '❌',
    color: '#dc3545',
    bgColor: '#f8d7da',
    borderColor: '#f5c6cb'
  }
} as const;

export const STATUS_CHANGE_OPTIONS: { value: StatusTarefa; label: string; icon: string }[] = [
  { value: 'PENDENTE', label: 'Marcar como Pendente', icon: '📋' },
  { value: 'CONCLUIDA', label: 'Marcar como Concluída', icon: '✅' },
  { value: 'CANCELADA', label: 'Marcar como Cancelada', icon: '❌' }
];

export const ANIMATION_DURATION = 300;
export const DEBOUNCE_DELAY = 500;
export const AUTO_HIDE_DELAY = 3000;

export const DATE_FORMAT_OPTIONS: Intl.DateTimeFormatOptions = {
  year: 'numeric',
  month: 'short',
  day: 'numeric',
  hour: '2-digit',
  minute: '2-digit'
};

export const TEXT_LIMITS = {
  titulo: {
    preview: 50,
    tooltip: 100
  },
  descricao: {
    preview: 120,
    tooltip: 200
  }
} as const;