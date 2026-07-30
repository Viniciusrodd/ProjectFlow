
// packages
package com.example.ProjectFlow.modules.auth.validator;

// imports
import org.springframework.stereotype.Component;

// import DTOs
import com.example.ProjectFlow.modules.auth.dto.RegisterDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;


@Component
public class RegisterValidator {
 
   // validate
   public void validate(RegisterDTO data) {
      
      // name
      if(data.name() == null | data.name().trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Nome é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      // name
      if(data.name().length() < ValidationConstants.MIN_NAME_LENGTH || data.name().length() > ValidationConstants.MAX_NAME_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Nome deve estar entre %d e %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MIN_NAME_LENGTH,
            ValidationConstants.MAX_NAME_LENGTH
         ));
      }

      // email
      if(data.email() == null | data.email().trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Email é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      // email
      if(!this.isValidEmail(data.email())) {
         throw MultiExceptions.invalid(String.format(
            "%s: Formato de email inválido",
            ResponseMessages.INVALID_DATA
         ));
      }

      // password
      if(data.password() == null | data.password().trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Senha é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      // password
      if(data.password().length() < ValidationConstants.MIN_PASSWORD_LENGTH || data.password().length() > ValidationConstants.MAX_PASSWORD_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Senha deve estar entre %d e %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MIN_PASSWORD_LENGTH,
            ValidationConstants.MAX_PASSWORD_LENGTH
         ));
      }

   }


   // email validation
   private boolean isValidEmail(String email) {
      String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
      return email.matches(emailRegex);
   }

}