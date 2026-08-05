
// packages
package com.example.ProjectFlow.modules.user.validator;

// imports
import org.springframework.stereotype.Component;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;

// import DTO
import com.example.ProjectFlow.modules.user.dto.UserUpdateDTO;


@Component
public class UserValidator {

   // properties
   String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

 
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

      if(!email.matches(this.emailRegex)) {
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


   // update validations
   public void updateValidations(UserUpdateDTO data) {
      // name
      data.name().ifPresent(name -> {
         if(name.trim().isEmpty()) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Nome não pode ser vazio",
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
      });

      // email
      data.email().ifPresent(email -> {
         if(email.trim().isEmpty()) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Email não pode ser vazio",
               ResponseMessages.BAD_REQUEST
            ));
         }

         if(!email.matches(this.emailRegex)) {
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
      });

      // password
      data.password().ifPresent(password -> {
         if(password.trim().isEmpty()) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Senha não pode ser vazia",
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
      });
   }

}