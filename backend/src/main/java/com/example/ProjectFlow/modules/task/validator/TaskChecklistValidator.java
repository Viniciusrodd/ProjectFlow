
// packages
package com.example.ProjectFlow.modules.task.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.taskChecklistDTO.TaskChecklistUpdateDTO;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;


@Component
public class TaskChecklistValidator {
 
   // id validate
   public void idValidate(UUID id) {
      if(id == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }
   
   
   // task id validate
   public void taskIdValidate(UUID taskId) {
      if(taskId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id de tarefa é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }


   // description validate
   public void descriptionValidate(String description) {
      if(description == null || description.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Descrição é obrigatória",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(description.length() > ValidationConstants.MAX_CHECKLIST_DESC_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Descrição deve ser no máximo %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MAX_CHECKLIST_DESC_LENGTH
         ));
      }
   }


   // completed validate - unnecessary
   

   // position validate
   public void positionValidate(Integer position) {
      if(position == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Posição é obrigatória",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(position <= 0) {
         throw MultiExceptions.invalid(String.format(
            "%s: Posição deve ser maior que 0",
            ResponseMessages.INVALID_DATA
         ));
      }
   }


   // update validations
   public void updateValidations(TaskChecklistUpdateDTO data) {
      // description validate
      Optional.ofNullable(data.description()).ifPresent(description -> {
         if(description.length() > ValidationConstants.MAX_CHECKLIST_DESC_LENGTH) {
            throw MultiExceptions.invalid(String.format(
               "%s: Descrição deve ser no máximo %d caracteres",
               ResponseMessages.INVALID_DATA,
               ValidationConstants.MAX_CHECKLIST_DESC_LENGTH
            ));
         }
      });


      // position validate
      Optional.ofNullable(data.position()).ifPresent(position -> {
         if(position <= 0) {
            throw MultiExceptions.invalid(String.format(
               "%s: Posição deve ser maior que 0",
               ResponseMessages.INVALID_DATA
            ));
         }
      });
   }

}