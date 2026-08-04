
// packages
package com.example.ProjectFlow.modules.user.service;

// imports
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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

// import entities
import com.example.ProjectFlow.modules.user.document.ProfileImagesDocument;


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
   public ProfileImageResponseDTO uploadProfileImage(Long userId, MultipartFile file) {
      // user existence - validation
      if(!this.userService.existsById(userId)) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      // image file - validation
      this.profileImageValidator.validate(file);

      try {
         // replace old image for new one
         if(this.profileImageRepository.existsByUserId(userId)) {
            this.profileImageRepository.deleteByUserId(userId);
         }

         // document - setup
         ProfileImagesDocument document = new ProfileImagesDocument.Builder()
            .userId(userId)
            .fileName(file.getOriginalFilename())
            .mimeType(file.getContentType())
            .size(file.getSize())
            .uploadDate(LocalDateTime.now())
            .binary(file.getBytes())
            .build();

         // save document - mongodb
         ProfileImagesDocument savedDocument = this.profileImageRepository.save(document);

         // update user profile image id - mysql
         this.userService.updateProfileImageId(userId, savedDocument.getId());

         // return saved document
         return ProfileImageResponseDTO.get(savedDocument);
      }
      catch (IOException error) {
         throw MultiExceptions.internal(String.format(
            "%s: Falha a   o processar imagem de usuário: %e", 
            ResponseMessages.INTERNAL_ERROR,
            error.getMessage()
         ));
      }
   }


   // get profile image
   public ProfileImagesDocument getProfileImage(Long userId) {
      // user existence - validation
      if(!this.userService.existsById(userId)) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      ProfileImagesDocument profileImage = this.profileImageRepository.findByUserId(userId);
      if(profileImage == null) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagem de perfil não existe",
            ResponseMessages.NOT_FOUND
         ));
      }

      return profileImage;
   }


   // get all profile images
   public List<ProfileImagesDocument> getAllProfileImages() {
      List<ProfileImagesDocument> profileImages = this.profileImageRepository.findAll();
      if(profileImages.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Imagens de perfil não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return profileImages;
   }

}