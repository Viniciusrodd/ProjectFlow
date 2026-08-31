
// packages
package com.example.ProjectFlow.modules.task.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Component
public class TaskLabelsValidator {
 
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


   // label id validate
   public void labelIdValidate(UUID labelId) {
      if(labelId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id da etiqueta é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }

}