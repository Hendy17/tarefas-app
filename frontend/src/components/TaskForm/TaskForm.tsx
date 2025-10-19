import React from 'react';
import { TaskFormProps } from './TaskForm.types';
import { useTaskForm } from './TaskForm.hooks';
import './TaskForm.styles.css';

export const TaskForm: React.FC<TaskFormProps> = (props) => {
  const {
    isOpen,
    task,
    onSave,
    onCancel
  } = props;

  const {
    formData,
    isLoading,
    errors,
    characterCounts,
    handleInputChange,
    handleTextareaChange,
    handleStatusChange,
    handleSubmit,
    getCharacterCountClass,
    STATUS_OPTIONS,
    MAX_LENGTHS
  } = useTaskForm({ task, onSave, onCancel });

  if (!isOpen) return null;

  return (
    <div className="task-form-overlay" onClick={onCancel}>
      <div 
        className="task-form-container"
        onClick={(e) => e.stopPropagation()}
        role="dialog"
        aria-modal="true"
        aria-labelledby="task-form-title"
      >
        
        <div className="task-form-header">
          <h2 id="task-form-title">
            {task ? '✏️ Editar Tarefa' : '📝 Nova Tarefa'}
          </h2>
          <button
            className="close-button"
            onClick={onCancel}
            disabled={isLoading}
            aria-label="Fechar formulário"
            type="button"
          >
            ✕
          </button>
        </div>


        <form className="task-form" onSubmit={handleSubmit} noValidate>

          <div className="form-group">
            <label htmlFor="titulo" className="form-label">
              Título <span className="required">*</span>
            </label>
            <input
              id="titulo"
              name="titulo"
              type="text"
              value={formData.titulo}
              onChange={handleInputChange}
              className={`form-input ${errors.titulo ? 'error' : ''}`}
              placeholder="Digite o título da tarefa..."
              maxLength={MAX_LENGTHS.titulo}
              disabled={isLoading}
              required
              aria-describedby={errors.titulo ? 'titulo-error' : 'titulo-count'}
            />
            
            
            <div 
              id="titulo-count"
              className={`character-count ${getCharacterCountClass('titulo', characterCounts.titulo)}`}
            >
              {characterCounts.titulo}/{MAX_LENGTHS.titulo}
            </div>
            
            
            {errors.titulo && (
              <div id="titulo-error" className="error-message" role="alert">
                {errors.titulo}
              </div>
            )}
          </div>

          
          <div className="form-group">
            <label htmlFor="descricao" className="form-label">
              Descrição <span className="required">*</span>
            </label>
            <textarea
              id="descricao"
              name="descricao"
              value={formData.descricao}
              onChange={handleTextareaChange}
              className={`form-textarea ${errors.descricao ? 'error' : ''}`}
              placeholder="Descreva a tarefa detalhadamente..."
              maxLength={MAX_LENGTHS.descricao}
              disabled={isLoading}
              required
              aria-describedby={errors.descricao ? 'descricao-error' : 'descricao-count'}
            />
            
            
            <div 
              id="descricao-count"
              className={`character-count ${getCharacterCountClass('descricao', characterCounts.descricao)}`}
            >
              {characterCounts.descricao}/{MAX_LENGTHS.descricao}
            </div>
            
            
            {errors.descricao && (
              <div id="descricao-error" className="error-message" role="alert">
                {errors.descricao}
              </div>
            )}
          </div>

          
          <div className="form-group">
            <fieldset>
              <legend className="form-label">
                Status <span className="required">*</span>
              </legend>
              
              <div className="status-options" role="radiogroup" aria-labelledby="status-label">
                {STATUS_OPTIONS.map((option: any) => (
                  <div key={option.value} className="status-option">
                    <input
                      type="radio"
                      id={`status-${option.value}`}
                      name="status"
                      value={option.value}
                      checked={formData.status === option.value}
                      onChange={handleStatusChange}
                      disabled={isLoading}
                      aria-describedby={`status-${option.value}-desc`}
                    />
                    <label 
                      htmlFor={`status-${option.value}`} 
                      className="status-label"
                    >
                      <span>{option.icon}</span>
                      <span>{option.label}</span>
                    </label>
                  </div>
                ))}
              </div>
              
              
              {errors.status && (
                <div className="error-message" role="alert">
                  {errors.status}
                </div>
              )}
            </fieldset>
          </div>

          
          <div className="form-actions">
            <button
              type="button"
              className="button button-secondary"
              onClick={onCancel}
              disabled={isLoading}
            >
              <span>✖️</span>
              Cancelar
            </button>

            <button
              type="submit"
              className="button button-primary"
              disabled={isLoading}
            >
              {isLoading ? (
                <>
                  <div className="spinner" aria-hidden="true" />
                  {task ? 'Atualizando...' : 'Salvando...'}
                </>
              ) : (
                <>
                  <span>{task ? '💾' : '✅'}</span>
                  {task ? 'Atualizar' : 'Salvar'}
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default TaskForm;