
// packages
package com.example.ProjectFlow.modules.user.service;

// imports
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// jakarta imports
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.user.repository.ProfileImageRepository;

// import validator
import com.example.ProjectFlow.modules.user.validator.ProfileImageValidator;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.ProfileImageResponseDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import document
import com.example.ProjectFlow.modules.user.document.ProfileImageDocument;


@Service
public class ProfileImageService {
 
   // properties
   ProfileImageRepository profileImageRepository;
   UserService userService;
   ProfileImageValidator profileImageValidator;

   
   // constructor - dependency injection
   public ProfileImageService(
      ProfileImageRepository profileImageRepository,
      UserService userService,
      ProfileImageValidator profileImageValidator
   ) {
      this.profileImageRepository = profileImageRepository;
      this.userService = userService;
      this.profileImageValidator = profileImageValidator;
   }


   // profile image upload
   @Transactional
   public ProfileImageResponseDTO uploadProfileImage(UUID userId, MultipartFile file) {
      this.userService.existsById(userId);
      this.profileImageValidator.validate(file);

      try {
         // replace old image for new one
         if(this.profileImageRepository.existsByUserId(userId)) {
            this.profileImageRepository.deleteByUserId(userId);
         }

         // document - setup
         ProfileImageDocument document = new ProfileImageDocument.Builder()
            .userId(userId)
            .fileName(file.getOriginalFilename())
            .mimeType(file.getContentType())
            .size(file.getSize())
            .uploadDate(LocalDateTime.now())
            .binary(file.getBytes())
            .build();

         // save document - mongodb
         ProfileImageDocument savedDocument = this.profileImageRepository.save(document);

         // update user profile image id - mysql
         this.userService.updateProfileImageId(userId, savedDocument.getId());

         // return saved document
         return ProfileImageResponseDTO.get(savedDocument);
      }
      catch (IOException error) {
         throw MultiExceptions.internal(String.format(
            "%s: Falha ao processar imagem de usuário: %s", 
            ResponseMessages.INTERNAL_ERROR,
            error.getMessage()
         ));
      }
   }


   // get profile image
   public ProfileImageDocument getProfileImage(UUID userId) {
      this.userService.existsById(userId);

      ProfileImageDocument profileImage = this.profileImageRepository.findByUserId(userId);
      if(profileImage == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagem de perfil não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return profileImage;
   }


   // get all profile images
   public List<ProfileImageDocument> getAllProfileImages() {
      List<ProfileImageDocument> profileImages = this.profileImageRepository.findAll();
      if(profileImages.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagens de perfil não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return profileImages;
   }


   // delete profile image
   public void deleteProfileImage(UUID userId) {
      this.userService.existsById(userId);

      // profile image existence - validation
      if(this.profileImageRepository.findByUserId(userId) == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagem de perfil não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      // delete profile image id - mysql
      this.userService.removeProfileImageId(userId);
      
      // delete profile image - mongodb
      this.profileImageRepository.deleteByUserId(userId);
   }

}