import { taskServiceApi } from './TaskService.api';

export { 
  taskServiceApi as default,
  taskServiceApi,
  validateTaskData,
  transformTaskForApi,
  transformTaskFromApi,
  getPerformanceMetrics,
  getAverageResponseTime,
  getErrorRate,
  clearPerformanceMetrics,
  testApiConnection
} from './TaskService.api';

export type {
  TaskService,
  TaskStats,
  UseTasksState,
  ApiConfig,
  ApiError,
  ApiErrorResponse,
  TaskFilter,
  RequestMetrics
} from './TaskService.types';

export {
  API_CONSTANTS,
  HttpStatus
} from './TaskService.types';

export {
  useTasks,
  useTaskFilter,
  useConnectionStatus,
  useApiMetrics
} from './TaskService.hooks';

export const taskService = taskServiceApi;