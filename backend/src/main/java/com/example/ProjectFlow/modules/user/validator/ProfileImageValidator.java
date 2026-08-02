
// packages
package com.example.ProjectFlow.modules.user.validator;

// imports
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
import com.example.ProjectFlow.common.constants.ValidationConstants;


@Component
public class ProfileImageValidator {
 
   public void validate(MultipartFile file) {

      // file
      if(file == null || file.isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Arquivo de imagem é obrigatório", 
            ResponseMessages.BAD_REQUEST
         ));
      }

      // size
      if(file.getSize() > ValidationConstants.MAX_IMAGE_SIZE) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Imagem excede o tamanho máximo de 5MB", 
            ResponseMessages.INVALID_DATA
         ));
      }

      // mime
      String mimeType = file.getContentType();
      if(mimeType == null || !Arrays.asList(ValidationConstants.ALLOWED_IMAGE_TYPES).contains(mimeType)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Formato de imagem não suportado. Use: JPEG, PNG ou WEBP", 
            ResponseMessages.INVALID_DATA
         ));
      }

      // extension
      String fileName = file.getOriginalFilename();
      String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
      List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
      
      if(!allowedExtensions.contains(extension)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Extensão de arquivo não suportada", 
            ResponseMessages.INVALID_DATA
         ));
      }

   }


   // profile id validation
   public void idValidate(String profileImageId) {
      if(profileImageId == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id de imagem é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }

      if(profileImageId.length() <= 0) {
         throw MultiExceptions.invalid(String.format(
            "%s: Id de imagem deve ser maior que 0",
            ResponseMessages.INVALID_DATA
         ));
      }
   }

}