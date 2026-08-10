
// packages
package com.example.ProjectFlow.modules.organization.service;

// imports
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.organization.repository.OrganizationImageRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationImageValidator;

// import DTOs
import com.example.ProjectFlow.modules.organization.dto.OrganizationImageResponseDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import document
import com.example.ProjectFlow.modules.organization.document.OrganizationImageDocument;


@Service
public class OrganizationImageService {
 
   // properties
   OrganizationImageRepository organizationImageRepository;
   OrganizationService organizationService;
   OrganizationImageValidator organizationImageValidator;
   
   
   // constructor - dependency injection
   public OrganizationImageService(
      OrganizationImageRepository organizationImageRepository,
      OrganizationService organizationService,
      OrganizationImageValidator organizationImageValidator
   ) {
      this.organizationImageRepository = organizationImageRepository;
      this.organizationService = organizationService;
      this.organizationImageValidator = organizationImageValidator;
   }


   // organization image upload
   @Transactional
   public OrganizationImageResponseDTO uploadOrganizationImage(UUID organizationId, MultipartFile file) {
      this.organizationService.existsById(organizationId);
      this.organizationImageValidator.validate(file);

      try {
         // replace old image for new one
         if(this.organizationImageRepository.existsByOrganizationId(organizationId)) {
            this.organizationImageRepository.deleteByOrganizationId(organizationId);
         }

         // document - setup
         OrganizationImageDocument document = new OrganizationImageDocument.Builder()
            .organizationId(organizationId)
            .fileName(file.getOriginalFilename())
            .mimeType(file.getContentType())
            .size(file.getSize())
            .uploadDate(LocalDateTime.now())
            .binary(file.getBytes())
            .build();

         // save document - mongodb
         OrganizationImageDocument savedDocument = this.organizationImageRepository.save(document);
         
         // update organization image id - mysql
         this.organizationService.updateLogoImageId(organizationId, savedDocument.getId());

         // return saved document
         return OrganizationImageResponseDTO.get(savedDocument);
      }
      catch (IOException error) {
         throw MultiExceptions.internal(String.format(
            "%s: Falha ao processar imagem da organização: %s", 
            ResponseMessages.INTERNAL_ERROR,
            error.getMessage()
         ));
      }

   }

}