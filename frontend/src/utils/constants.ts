export const API_CONFIG = {
  BASE_URL: process.env.REACT_APP_API_URL || '',
  TIMEOUT: 10000,
  ENDPOINTS: {
    TASKS: '/tasks',
    HEALTH: '/actuator/health'
  }
};

export const STATUS_CONFIG = {
  PENDENTE: { 
    label: '📋 Pendente', 
    color: '#ffc107',
    bgColor: '#fff3cd',
    textColor: '#856404'
  },
  EM_ANDAMENTO: { 
    label: '⚡ Em Andamento', 
    color: '#17a2b8',
    bgColor: '#d1ecf1',
    textColor: '#0c5460'
  },
  CONCLUIDA: { 
    label: '✅ Concluída', 
    color: '#28a745',
    bgColor: '#d4edda',
    textColor: '#155724'
  }
} as const;

export const VALIDATION_CONFIG = {
  TITULO: {
    MIN_LENGTH: 3,
    MAX_LENGTH: 100
  },
  DESCRICAO: {
    MIN_LENGTH: 5,
    MAX_LENGTH: 500
  }
} as const;