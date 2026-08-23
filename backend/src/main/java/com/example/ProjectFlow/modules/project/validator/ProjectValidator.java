
// packages
package com.example.ProjectFlow.modules.project.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.Arrays;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;

// import enums
import com.example.ProjectFlow.modules.project.enums.StatusEnum;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.ProjectUpdateDTO;


@Component
public class ProjectValidator {
   
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


   // owner id validate
   public void ownerIdValidate(UUID ownerId) {
      if(ownerId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id de dono do projeto é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }


   // name validations
   public void nameValidate(String name) {
      if(name == null || name.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Nome é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(name.length() < ValidationConstants.MIN_PROJECT_NAME_LENGTH || name.length() > ValidationConstants.MAX_PROJECT_NAME_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Nome deve estar entre %d e %d caracteres",
            ResponseMessages.INVALID_DATA,
            ValidationConstants.MIN_PROJECT_NAME_LENGTH,
            ValidationConstants.MAX_PROJECT_NAME_LENGTH
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


   // status validations
   public void statusValidate(String status) {
      if(status == null || status.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Status do projeto é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(!StatusEnum.isValid(status)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Status de projeto inválido. Valores permitidos: %s",
            ResponseMessages.BAD_REQUEST,
            Arrays.toString(StatusEnum.values())
         ));
      }
   }

   
   // logo image id validations
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


   // update validations
   public void updateValidations(ProjectUpdateDTO data) {
      // name
      Optional.ofNullable(data.name()).ifPresent(name -> {
         if(name.trim().isEmpty()) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Nome não pode ser vazio",
               ResponseMessages.BAD_REQUEST
            ));
         }

         if(name.length() < ValidationConstants.MIN_PROJECT_NAME_LENGTH || name.length() > ValidationConstants.MAX_PROJECT_NAME_LENGTH) {
            throw MultiExceptions.invalid(String.format(
               "%s: Nome deve estar entre %d e %d caracteres",
               ResponseMessages.INVALID_DATA,
               ValidationConstants.MIN_PROJECT_NAME_LENGTH,
               ValidationConstants.MAX_PROJECT_NAME_LENGTH
            ));
         }
      });

      // description
      Optional.ofNullable(data.description()).ifPresent(description -> 
         this.descriptionValidate(description)
      );
   }

}