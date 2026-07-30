
// packages
package com.example.ProjectFlow.modules.auth.validator;

// imports
import org.springframework.stereotype.Component;

// import DTOs
import com.example.ProjectFlow.modules.auth.dto.LoginDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Component
public class LoginValidator {
 
   // validate
   public void validate(LoginDTO data) {

      // email
      if(data.email() == null | data.email().trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Email é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      // password
      if(data.password() == null | data.password().trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Senha é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      // check if user exists...

   }

}