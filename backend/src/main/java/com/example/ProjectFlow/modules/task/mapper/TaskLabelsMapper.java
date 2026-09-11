
// packages
package com.example.ProjectFlow.modules.task.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.task.entity.TaskLabelsEntity;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TasksByLabelResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.LabelsByTaskResponseDTO;
import com.example.ProjectFlow.modules.task.dto.taskLabelsDTO.TaskLabelsDeletedDTO;

// import mapper
import com.example.ProjectFlow.modules.labels.mapper.LabelsMapper;


@Component 
public class TaskLabelsMapper {

   // properties
   private final TaskMapper taskMapper;
   private final LabelsMapper labelsMapper;
   

   // constructor - dependency injection
   public TaskLabelsMapper(
      TaskMapper taskMapper,
      LabelsMapper labelsMapper
   ) {
      this.taskMapper = taskMapper;
      this.labelsMapper = labelsMapper;
   }

 
   // from TaskLabelsEntity to TaskLabelsResponseDTO
   public TaskLabelsResponseDTO toTaskLabelsResponseDTO(TaskLabelsEntity entity) {
      return new TaskLabelsResponseDTO(
         entity.getId(),
         entity.getTask().getId(),
         entity.getLabel().getId(),
         entity.getCreatedAt()
      );
   }


   // from TaskLabelsEntity to TasksByLabelResponseDTO
   public TasksByLabelResponseDTO toTasksByLabelResponseDTO(TaskLabelsEntity entity) {
      return new TasksByLabelResponseDTO(
         entity.getId(),
         this.taskMapper.toTasksResponseDTO(entity.getTask())
      );
   }


   // from TaskLabelsEntity to TaskLabelsDeletedDTO
   public TaskLabelsDeletedDTO toTaskLabelsDeletedDTO(TaskLabelsEntity entity) {
      return new TaskLabelsDeletedDTO(
         entity.getId(),
         entity.getTask().getId(),
         entity.getLabel().getId(),
         entity.getCreatedAt(),
         entity.getDeletedAt()
      );
   }


   // from TaskLabelsEntity to LabelsByTaskResponseDTO
   public LabelsByTaskResponseDTO toLabelsByTaskResponseDTO(TaskLabelsEntity entity) {
      return new LabelsByTaskResponseDTO(
         entity.getId(),
         this.labelsMapper.toLabelsResponseDTO(entity.getLabel())
      );
   }

}