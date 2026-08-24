
// packages
package com.example.ProjectFlow.modules.task.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;

// import DTOs
import com.example.ProjectFlow.modules.task.dto.TasksUpdateDTO;

// import enums
import com.example.ProjectFlow.modules.task.enums.PriorityEnum;


@Component
public class TasksValidator {
   
   // id validate
   public void idValidate(UUID id) {
      if(id == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }


   // project id validate
   public void projectIdValidate(UUID projectId) {
      if(projectId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id de projeto é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }


   // column id validate
   public void columnIdValidate(UUID columnId) {
      if(columnId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id da coluna do quadro é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }


   // title validate
   public void titleValidate(String title) {
      if(title == null || title.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Título é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(title.length() < ValidationConstants.MIN_TITLE_LENGTH || title.length() > ValidationConstants.MAX_TITLE_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Título deve estar entre %d e %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MIN_TITLE_LENGTH,
            ValidationConstants.MAX_TITLE_LENGTH
         ));
      }
   }
   

   // description validate
   public void descriptionValidate(String description) {
      if(description.length() > ValidationConstants.MAX_DESCRIPTION_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Descrição deve ser no máximo %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MAX_DESCRIPTION_LENGTH
         ));
      }
   }


   // priority validations
   public void priorityValidate(String priority) {
      if(priority == null || priority.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Prioridade de tarefa é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(!PriorityEnum.isValid(priority)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Prioridade de tarefa inválido. Valores permitidos: %s",
            ResponseMessages.BAD_REQUEST,
            Arrays.toString(PriorityEnum.values())
         ));
      }
   }


   // due date validations
   public void dueDateValidate(LocalDate dueDate) {
      if(dueDate == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Data de prazo é obrigatória",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(dueDate.isBefore(LocalDate.now())) {
         throw MultiExceptions.invalid(String.format(
            "%s: Data de prazo não pode ser anterior à data atual",
            ResponseMessages.INVALID_DATA
         ));
      }

      if(dueDate.isAfter(LocalDate.now().plusYears(1))) {
         throw MultiExceptions.invalid(String.format(
            "%s: Data de prazo não pode ser superior a 1 ano a partir de hoje",
            ResponseMessages.INVALID_DATA
         ));
      }
   }


   // update validations
   public void updateValidations(TasksUpdateDTO data) {
      // title
      Optional.ofNullable(data.title()).ifPresent(title -> {
         if(title.trim().isEmpty()) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Título não pode ser vazio",
               ResponseMessages.BAD_REQUEST
            ));
         }

         if(title.length() < ValidationConstants.MIN_TITLE_LENGTH || title.length() > ValidationConstants.MAX_TITLE_LENGTH) {
            throw MultiExceptions.invalid(String.format(
               "%s: Título deve estar entre %d e %d caracteres",
               ResponseMessages.INVALID_DATA,
               ValidationConstants.MIN_TITLE_LENGTH,
               ValidationConstants.MAX_TITLE_LENGTH
            ));
         }
      });

      // description
      Optional.ofNullable(data.description()).ifPresent(description -> {
         this.descriptionValidate(description);
      });

      // priority
      Optional.ofNullable(data.priority()).ifPresent(priority -> {
         if(priority.trim().isEmpty()) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Prioridade de tarefa não pode ser vazio",
               ResponseMessages.BAD_REQUEST
            ));
         }

         if(!PriorityEnum.isValid(priority)) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Prioridade de tarefa inválido. Valores permitidos: %s",
               ResponseMessages.BAD_REQUEST,
               Arrays.toString(PriorityEnum.values())
            ));
         }
      });

      // due date
      Optional.ofNullable(data.dueDate()).ifPresent(dueDate -> {
         if(dueDate.isBefore(LocalDate.now())) {
            throw MultiExceptions.invalid(String.format(
               "%s: Data de prazo não pode ser anterior à data atual",
               ResponseMessages.INVALID_DATA
            ));
         }

         if(dueDate.isAfter(LocalDate.now().plusYears(1))) {
            throw MultiExceptions.invalid(String.format(
               "%s: Data de prazo não pode ser superior a 1 ano a partir de hoje",
               ResponseMessages.INVALID_DATA
            ));
         }
      });
   }

}