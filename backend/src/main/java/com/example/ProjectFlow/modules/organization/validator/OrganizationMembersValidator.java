
// packages
package com.example.ProjectFlow.modules.organization.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import enum
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;


@Component
public class OrganizationMembersValidator {
 
   // id validate
   public void idValidate(UUID id) {
      if(id == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }

   // organization id validate
   public void organizationIdValidate(UUID organizationId) {
      if(organizationId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id de organização é obrigatório",
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

      if(!RoleEnum.isValid(role) || RoleEnum.OWNER.getType().equalsIgnoreCase(role)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Papel de usuário inválido. Valores permitidos: %s, %s",
            ResponseMessages.BAD_REQUEST,
            RoleEnum.ADMIN,
            RoleEnum.MEMBER
         ));
      }
   }

}