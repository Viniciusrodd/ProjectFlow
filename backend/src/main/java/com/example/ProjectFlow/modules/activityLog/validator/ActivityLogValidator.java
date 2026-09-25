
// packages
package com.example.ProjectFlow.modules.activityLog.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


@Component 
public class ActivityLogValidator {
 
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


   // action validate
   public void actionValidate(String action) {
      if(action == null || action.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Ação é obrigatória",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(!ActivityActionEnum.isValid(action)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Ação inválida. Valores permitidos: %s",
            ResponseMessages.BAD_REQUEST,
            Arrays.toString(ActivityActionEnum.values())
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

}