
// packages
package com.example.ProjectFlow.modules.labels.validator;

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
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsUpdateDTO;


@Component
public class LabelsValidator {
 
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
            "%s: Id de projeto é obrigatório",
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

      if(name.length() > ValidationConstants.MAX_LABEL_NAME_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Nome deve ser menor que %d caracteres",
            ResponseMessages.BAD_REQUEST,
            ValidationConstants.MAX_LABEL_NAME_LENGTH
         ));
      }
   }


   // color validations
   public void colorValidate(String color) {
      if(color == null || color.trim().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Cor é obrigatória",
            ResponseMessages.BAD_REQUEST
         ));
      }

      String hexColor = color.trim();
      if(!hexColor.matches("^#[0-9A-Fa-f]{6}$")) {
         throw MultiExceptions.invalid(String.format(
            "%s: Cores apenas em formato hexadecimal #RRGGBB",
            ResponseMessages.INVALID_DATA
         ));
      }
   }


   // update validations
   public void updateValidation(LabelsUpdateDTO data) {
      // name
      Optional.ofNullable(data.name()).ifPresent(name -> {
         if(name.trim().isEmpty()) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Nome não pode ser vazio",
               ResponseMessages.BAD_REQUEST
            ));
         }

         if(name.length() > ValidationConstants.MAX_LABEL_NAME_LENGTH) {
            throw MultiExceptions.invalid(String.format(
               "%s: Nome deve ser menor que %d caracteres",
               ResponseMessages.BAD_REQUEST,
               ValidationConstants.MAX_LABEL_NAME_LENGTH
            ));
         }
      });

      // color
      Optional.ofNullable(data.color()).ifPresent(color -> {
         if(color.trim().isEmpty()) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Campo de cor não pode ser vazio",
               ResponseMessages.BAD_REQUEST
            ));
         }

         String hexColor = color.trim();
         if(!hexColor.matches("^#[0-9A-Fa-f]{6}$")) {
            throw MultiExceptions.invalid(String.format(
               "%s: Cores apenas em formato hexadecimal #RRGGBB",
               ResponseMessages.INVALID_DATA
            ));
         }
      });
   }

}