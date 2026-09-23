
// packages
package com.example.ProjectFlow.modules.auth.validator;

// imports
import org.springframework.stereotype.Component;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Component
public class LoginValidator {

   // email validate
   public void emailValidate(String email) {
      if(email == null || email.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Email é obrigatório",
            ResponseMessages.BAD_REQUEST
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
   }

}