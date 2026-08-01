
// packages
package com.example.ProjectFlow.modules.user.validator;

// imports
import org.springframework.stereotype.Component;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Component
public class UserValidator {
 
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
   }   

}