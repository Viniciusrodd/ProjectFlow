
// packages
package com.example.ProjectFlow.modules.board.validator;

// imports
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;
import com.example.ProjectFlow.modules.board.dto.BoardColumnsUpdateDTO;
// import enum
import com.example.ProjectFlow.modules.board.enums.BoardEnum;


@Component
public class BoardColumnsValidator {
 
   // id validate
   public void idValidate(UUID id) {
      if(id == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   }


   // board id validate
   public void boardIdValidate(UUID boardId) {
      if(boardId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id do quadro é obrigatório",
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

      if(name.length() < ValidationConstants.MIN_BOARD_NAME_LENGTH || name.length() > ValidationConstants.MAX_BOARD_NAME_LENGTH) {
         throw MultiExceptions.invalid(String.format(
            "%s: Nome deve estar entre %d e %d caracteres",
            ValidationConstants.MIN_BOARD_NAME_LENGTH,
            ValidationConstants.MAX_BOARD_NAME_LENGTH
         ));
      }

      if(!BoardEnum.isValid(name)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Nome de quadro inválido. Valores permitidos: %s",
            ResponseMessages.BAD_REQUEST,
            Arrays.toString(BoardEnum.values())
         ));
      }
   }


   // position validations
   public void positionValidate(Integer position) {
      if(position == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Posição é obrigatória",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(position <= 0) {
         throw MultiExceptions.invalid(String.format(
            "%s: Posição deve ser maior que 0",
            ResponseMessages.INVALID_DATA
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
   public void updateValidations(BoardColumnsUpdateDTO data) {
      // name
      Optional.ofNullable(data.name()).ifPresent(name -> {
         if(name.trim().isEmpty()) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Nome não pode ser vazio",
               ResponseMessages.BAD_REQUEST
            ));
         }

         if(name.length() < ValidationConstants.MIN_BOARD_NAME_LENGTH || name.length() > ValidationConstants.MAX_BOARD_NAME_LENGTH) {
            throw MultiExceptions.invalid(String.format(
               "%s: Nome deve estar entre %d e %d caracteres",
               ValidationConstants.MIN_BOARD_NAME_LENGTH,
               ValidationConstants.MAX_BOARD_NAME_LENGTH
            ));
         }

         if(!BoardEnum.isValid(name)) {
            throw MultiExceptions.badRequest(String.format(
               "%s: Nome de quadro inválido. Valores permitidos: %s",
               ResponseMessages.BAD_REQUEST,
               Arrays.toString(BoardEnum.values())
            ));
         }
      });

      // position
      Optional.ofNullable(data.position()).ifPresent(position -> {
         if(position <= 0) {
            throw MultiExceptions.invalid(String.format(
               "%s: Posição deve ser maior que 0",
               ResponseMessages.INVALID_DATA
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