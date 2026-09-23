
// packages
package com.example.ProjectFlow.modules.auth.validator;

// imports
import org.springframework.stereotype.Component;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;


@Component
public class RegisterValidator {

   // name validate
   public void nameValidate(String name) {
      if(name == null || name.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Nome é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   
      if(name.length() < ValidationConstants.MIN_NAME_LENGTH || name.length() > ValidationConstants.MAX_NAME_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Nome deve estar entre %d e %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MIN_NAME_LENGTH,
            ValidationConstants.MAX_NAME_LENGTH
         ));
      }
   }


   // email validate
   public void emailValidate(String email) {
      if(email == null || email.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Email é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   
      if(!this.isValidEmail(email)) {
         throw MultiExceptions.invalid(String.format(
            "%s: Formato de email inválido",
            ResponseMessages.INVALID_DATA
         ));
      }
   }


   // password validate
   public void passwordValidate(String password) {
      if(password == null || password.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Senha é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   
      if(password.length() < ValidationConstants.MIN_PASSWORD_LENGTH || password.length() > ValidationConstants.MAX_PASSWORD_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Senha deve estar entre %d e %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MIN_PASSWORD_LENGTH,
            ValidationConstants.MAX_PASSWORD_LENGTH
         ));
      }
   }


   //// private


   // email validation
   private boolean isValidEmail(String email) {
      String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
      return email.matches(emailRegex);
   }

}