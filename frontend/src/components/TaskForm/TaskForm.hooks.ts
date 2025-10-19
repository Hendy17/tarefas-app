import { useState, useEffect, useCallback } from 'react';
import { TarefaDTO } from '../../types/Task';
import { TaskFormData, TaskFormErrors, VALIDATION_RULES, MAX_LENGTHS, STATUS_OPTIONS, UseTaskFormParams } from './TaskForm.types';

export const useTaskForm = ({ task, onSave, onCancel }: UseTaskFormParams) => {
  const [formData, setFormData] = useState<TaskFormData>({
    titulo: '',
    descricao: '',
    status: 'PENDENTE'
  });
  
  const [errors, setErrors] = useState<TaskFormErrors>({});
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (task) {
      setFormData({
        titulo: task.titulo,
        descricao: task.descricao,
        status: task.status
      });
    } else {
      setFormData({
        titulo: '',
        descricao: '',
        status: 'PENDENTE'
      });
    }
    setErrors({});
  }, [task]);

  const characterCounts = {
    titulo: formData.titulo.length,
    descricao: formData.descricao.length
  };

  const getCharacterCountClass = (field: keyof typeof characterCounts, count: number) => {
    const maxLength = MAX_LENGTHS[field];
    const percentage = count / maxLength;
    
    if (percentage >= 0.9) return 'danger';
    if (percentage >= 0.7) return 'warning';
    return 'normal';
  };

  const validateForm = useCallback((): boolean => {
    const newErrors: TaskFormErrors = {};

    if (!formData.titulo.trim()) {
      newErrors.titulo = VALIDATION_RULES.titulo.required;
    } else if (formData.titulo.length < VALIDATION_RULES.titulo.minLength) {
      newErrors.titulo = `Título deve ter pelo menos ${VALIDATION_RULES.titulo.minLength} caracteres`;
    } else if (formData.titulo.length > MAX_LENGTHS.titulo) {
      newErrors.titulo = `Título não pode exceder ${MAX_LENGTHS.titulo} caracteres`;
    }

    if (!formData.descricao.trim()) {
      newErrors.descricao = VALIDATION_RULES.descricao.required;
    } else if (formData.descricao.length < VALIDATION_RULES.descricao.minLength) {
      newErrors.descricao = `Descrição deve ter pelo menos ${VALIDATION_RULES.descricao.minLength} caracteres`;
    } else if (formData.descricao.length > MAX_LENGTHS.descricao) {
      newErrors.descricao = `Descrição não pode exceder ${MAX_LENGTHS.descricao} caracteres`;
    }

    if (!formData.status) {
      newErrors.status = VALIDATION_RULES.status.required;
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  }, [formData]);

  const handleInputChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    if (errors[name as keyof TaskFormErrors]) {
      setErrors(prev => ({
        ...prev,
        [name]: undefined
      }));
    }
  }, [errors]);

  const handleTextareaChange = useCallback((e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    if (errors[name as keyof TaskFormErrors]) {
      setErrors(prev => ({
        ...prev,
        [name]: undefined
      }));
    }
  }, [errors]);

  const handleStatusChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setFormData(prev => ({
      ...prev,
      status: value as TaskFormData['status']
    }));
    
    if (errors.status) {
      setErrors(prev => ({
        ...prev,
        status: undefined
      }));
    }
  }, [errors.status]);

  const handleSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setIsLoading(true);
    
    try {
      const taskData: TarefaDTO = {
        titulo: formData.titulo.trim(),
        descricao: formData.descricao.trim(),
        status: formData.status
      };

      await onSave(taskData);
      onCancel(); 
    } catch (error) {
      console.error('Erro ao salvar tarefa:', error);
    } finally {
      setIsLoading(false);
    }
  }, [formData, validateForm, onSave, onCancel]);

  const resetForm = useCallback(() => {
    setFormData({
      titulo: '',
      descricao: '',
      status: 'PENDENTE'
    });
    setErrors({});
    setIsLoading(false);
  }, []);

  return {
    formData,
    errors,
    isLoading,
    characterCounts,
    handleInputChange,
    handleTextareaChange,
    handleStatusChange,
    handleSubmit,
    getCharacterCountClass,
    validateForm,
    resetForm,
    STATUS_OPTIONS,
    MAX_LENGTHS
  };
};