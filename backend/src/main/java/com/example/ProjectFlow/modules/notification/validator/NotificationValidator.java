
// packages
package com.example.ProjectFlow.modules.notification.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;


@Component 
public class NotificationValidator {
 
   // id validate
   public void idValidate(String id) {
      if(id == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }


   // user id validate
   public void userIdValidate(UUID userId) {
      if(userId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id de usuário é obrigatório",
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


   // message validate
   public void messageValidate(String message) {
      if(message.length() == 0) {
         throw MultiExceptions.invalid(String.format(
            "%s: Mensagem não pode ser vazio",
            ResponseMessages.INVALID_DATA
         ));
      }

      if(message.length() > ValidationConstants.MAX_MESSAGE_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Mensagem deve ser no máximo %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MAX_MESSAGE_LENGTH
         ));
      }
   }


   // read validate - unnecessary

}