
// packages
package com.example.ProjectFlow.modules.board.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.Arrays;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;


@Component
public class BoardValidator {
 
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

   // name validations
   public void nameValidate(String name) {
      if(name == null || name.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Nome é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(name.length() < ValidationConstants.MIN_BOARD_NAME_LENGTH || name.length() > ValidationConstants.MAX_BOARD_NAME_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Nome deve estar entre %d e %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MIN_BOARD_NAME_LENGTH,
            ValidationConstants.MAX_BOARD_NAME_LENGTH
         ));
      }
   }

}