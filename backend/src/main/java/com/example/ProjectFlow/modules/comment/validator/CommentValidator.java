
// packages
package com.example.ProjectFlow.modules.comment.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.UUID;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;


@Component
public class CommentValidator {
 
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


   // author id validate
   public void authorIdValidate(UUID authorId) {
      if(authorId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id de autor é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }


   // content validate
   public void contentValidate(String content) {
      if(content.length() == 0) {
         throw MultiExceptions.invalid(String.format(
            "%s: Comentário não pode ser vazio",
            ResponseMessages.INVALID_DATA
         ));
      }

      if(content.length() > ValidationConstants.MAX_COMMENT_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Comentário deve ser no máximo %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MAX_COMMENT_LENGTH
         ));
      }
   }

}