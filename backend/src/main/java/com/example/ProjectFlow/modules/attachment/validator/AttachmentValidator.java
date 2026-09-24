
// packages
package com.example.ProjectFlow.modules.attachment.validator;

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
public class AttachmentValidator {
   
   // id validation
   public void idValidate(String id) {
      if(id == null || id.isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Id do arquivo é obrigatório",
            ResponseMessages.BAD_REQUEST
         ));
      }
   
      if(id.length() <= 0) {
         throw MultiExceptions.invalid(String.format(
            "%s: Id do arquivo deve ser maior que 0",
            ResponseMessages.INVALID_DATA
         ));
      }
   }


   public void validate(MultipartFile file) {
      // file
      if(file == null || file.isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Arquivo é obrigatório", 
            ResponseMessages.BAD_REQUEST
         ));
      }

      // filename
      if(file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Nome de arquivo é obrigatório", 
            ResponseMessages.BAD_REQUEST
         ));
      }

      // size
      if(file.getSize() > ValidationConstants.MAX_ATTACHMENT_SIZE) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Arquivo excede o tamanho máximo de 25MB", 
            ResponseMessages.INVALID_DATA
         ));
      }

      // mime
      String mimeType = file.getContentType();
      if(mimeType == null || !Arrays.asList(ValidationConstants.ALLOWED_ATTACHMENT_TYPES).contains(mimeType)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Formato de arquivo não suportado. Use: %s", 
            ResponseMessages.INVALID_DATA,
            Arrays.toString(ValidationConstants.ALLOWED_ATTACHMENT_TYPES)
         ));
      }

      // extension
      String fileName = file.getOriginalFilename();
      String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
      List<String> allowedExtensions = Arrays.asList(ValidationConstants.ALLOWED_ATTACHMENT_EXTENSIONS);
      
      if(!allowedExtensions.contains(extension)) {
         throw MultiExceptions.badRequest(String.format(
            "%s: Extensão de arquivo não suportada. Use: %s", 
            ResponseMessages.INVALID_DATA,
            Arrays.toString(ValidationConstants.ALLOWED_ATTACHMENT_EXTENSIONS)
         ));
      }
   }

}