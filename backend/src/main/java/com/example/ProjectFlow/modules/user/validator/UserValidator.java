
// packages
package com.example.ProjectFlow.modules.user.validator;

// imports
import org.springframework.stereotype.Component;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;


@Component
public class UserValidator {
 
   // id validate
   public void idValidate(Long id) {
      if(id == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(id <= 0) {
         throw MultiExceptions.invalid(String.format(
            "%s: Id deve ser maior que 0",
            ResponseMessages.INVALID_DATA
         ));
      }
   }
   
   
   // email validate
   public void emailValidate(String email) {
      if(email == null || email.isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Email é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
      if(!email.matches(emailRegex)) {
         throw MultiExceptions.invalid(String.format(
            "%s: Formato de email inválido",
            ResponseMessages.INVALID_DATA
         ));
      }

      if(email.length() > ValidationConstants.MAX_EMAIL_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Email deve ser menor que %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MAX_EMAIL_LENGTH
         ));
      }
   }

}