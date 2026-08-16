
// packages
package com.example.ProjectFlow.modules.project.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import enum
import com.example.ProjectFlow.modules.project.enums.RoleEnum;


@Component
public class ProjectMembersValidator {
 
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
            "%s: Id do projeto é obrigatório",
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

   // role validations
   public void roleValidate(String role) {
      if(role == null || role.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Papel de usuário é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(!RoleEnum.isValid(role)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Papel de usuário inválido. Valores permitidos: %s",
            ResponseMessages.BAD_REQUEST,
            Arrays.toString(RoleEnum.values())
         ));
      }
   }

}