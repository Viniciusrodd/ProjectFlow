
// packages
package com.example.ProjectFlow.modules.organization.validator;

// imports
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationUpdateDTO;


@Component
public class OrganizationValidator {
 
   // id validate
   public void idValidate(UUID id) {
      if(id == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }


   // ownerId validate
   public void ownerIdValidate(UUID ownerId) {
      if(ownerId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id do dono é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }


   // logo image id validate
   public void logoImageIdValidate(String logoImageId) {
      if(logoImageId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id da logo é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(logoImageId.length() < 0) {
         throw MultiExceptions.invalid(String.format(
            "%s: Id da logo deve ser maior que 0",
            ResponseMessages.INVALID_DATA
         ));
      }
   }


   // name validate
   public void nameValidate(String name) {
      if(name == null || name.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Nome é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(name.length() < ValidationConstants.MIN_ORGANIZATION_NAME_LENGTH || name.length() > ValidationConstants.MAX_ORGANIZATION_NAME_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Nome deve estar entre %d e %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MIN_NAME_LENGTH,
            ValidationConstants.MAX_NAME_LENGTH
         ));
      }
   }


   // description validate
   public void descriptionValidate(String description) {
      if(description.length() > ValidationConstants.MAX_DESCRIPTION_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Descrição deve ser no máximo %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MAX_DESCRIPTION_LENGTH
         ));
      }
   }


   // update validations
   public void updateValidations(OrganizationUpdateDTO data) {
      // name
      Optional.ofNullable(data.name()).ifPresent(name -> {
         if(name.trim().isEmpty()) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Nome é não pode ser vazio",
               ResponseMessages.BAD_REQUEST
            ));
         }

         if(name.length() < ValidationConstants.MIN_ORGANIZATION_NAME_LENGTH || name.length() > ValidationConstants.MAX_ORGANIZATION_NAME_LENGTH) {
            throw MultiExceptions.invalid(String.format(
               "%s: Nome deve estar entre %d e %d caracteres",
               ResponseMessages.INVALID_DATA,
               ValidationConstants.MIN_NAME_LENGTH,
               ValidationConstants.MAX_NAME_LENGTH
            ));
         }
      });

      // description
      Optional.ofNullable(data.description()).ifPresent(description -> 
         this.descriptionValidate(description)
      );
   }

}