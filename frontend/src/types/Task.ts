// Tipos TypeScript baseados na API Spring Boot
export interface Tarefa {
  id?: number;
  titulo: string;
  descricao: string;
  status: StatusTarefa;
  dataCriacao?: string;
  dataAtualizacao?: string;
}

export type StatusTarefa = 'PENDENTE' | 'CONCLUIDA' | 'CANCELADA';

export interface TarefaDTO {
  titulo: string;
  descricao: string;
  status: StatusTarefa;
}

export interface TarefaResponseDTO {
  id: number;
  titulo: string;
  descricao: string;
  status: StatusTarefa;
  dataCriacao: string;
  dataAtualizacao: string;
}

export interface ApiError {
  message: string;
  details?: string[];
  timestamp?: string;
  status?: number;
}