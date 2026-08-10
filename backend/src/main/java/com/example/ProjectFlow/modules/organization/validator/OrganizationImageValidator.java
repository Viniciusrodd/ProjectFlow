
// packages
package com.example.ProjectFlow.modules.organization.validator;

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
public class OrganizationImageValidator {
 
   // id validation
   public void idValidate(String id) {
      if(id == null) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id da imagem é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   
      if(id.length() <= 0) {
         throw MultiExceptions.invalid(String.format(
            "%s: Id da imagem deve ser maior que 0",
            ResponseMessages.INVALID_DATA
         ));
      }
   }


   public void validate(MultipartFile file) {
      // file
      if(file == null || file.isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Arquivo de imagem é obrigatório", 
            ResponseMessages.BAD_REQUEST
         ));
      }

      // size
      if(file.getSize() > ValidationConstants.MAX_ORGANIZATION_IMAGE_SIZE) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Imagem excede o tamanho máximo de 10MB", 
            ResponseMessages.INVALID_DATA
         ));
      }

      // mime
      String mimeType = file.getContentType();
      if(mimeType == null || !Arrays.asList(ValidationConstants.ALLOWED_IMAGE_TYPES).contains(mimeType)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Formato de imagem não suportado. Use: JPG, JPEG, PNG ou WEBP", 
            ResponseMessages.INVALID_DATA
         ));
      }

      // extension
      String fileName = file.getOriginalFilename();
      String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
      List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "webp");
      
      if(!allowedExtensions.contains(extension)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Extensão de arquivo não suportada", 
            ResponseMessages.INVALID_DATA
         ));
      }
   }

}