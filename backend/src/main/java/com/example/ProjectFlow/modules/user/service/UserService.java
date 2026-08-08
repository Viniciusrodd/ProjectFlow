
// packages
package com.example.ProjectFlow.modules.user.service;

// imports
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

// jakart imports
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.user.repository.UserRepository;

// import validator
import com.example.ProjectFlow.modules.user.validator.UserValidator;
import com.example.ProjectFlow.modules.user.validator.ProfileImageValidator;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserDTO;
import com.example.ProjectFlow.modules.user.dto.UserProfileDTO;
import com.example.ProjectFlow.modules.user.dto.UserUpdateDTO;
import com.example.ProjectFlow.modules.user.entity.UserEntity;
import com.example.ProjectFlow.modules.user.dto.UserDeletedDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import services
import com.example.ProjectFlow.modules.auth.service.PasswordService;


@Service
public class UserService {

   // properties
   private final UserRepository userRepository;
   private final UserValidator userValidator;
   private final ProfileImageValidator profileImageValidator;
   private final PasswordService passwordService;

   // constructor - dependency injection
   public UserService(
      UserRepository userRepository,
      UserValidator userValidator,
      ProfileImageValidator profileImageValidator,
      PasswordService passwordService
   ) {
      this.userRepository = userRepository;
      this.userValidator = userValidator;
      this.profileImageValidator = profileImageValidator;
      this.passwordService = passwordService;
   }


   // get all users
   public List<UserProfileDTO> getAll() {
      List<UserProfileDTO> users = this.userRepository.getAll();

      if(users.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuários não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return users;
   }


   // get by id
   public UserProfileDTO getById(UUID id) {
      this.userValidator.idValidate(id);

      try {
         return this.userRepository.getById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // get entity by id
   public UserEntity getEntityById(UUID id) {
      this.userValidator.idValidate(id);

      try {
         return this.userRepository.getEntityById(id);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }
   
   
   // get by email
   public UserDTO getByEmail(String email) {
      this.userValidator.emailValidate(email);

      try {
         return this.userRepository.getByEmail(email);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // exists by id
   public boolean existsById(UUID id) {
      this.userValidator.idValidate(id);

      boolean exist = this.userRepository.existsById(id);
      if(!exist) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe",
            ResponseMessages.NOT_FOUND
         ));
      }
      
      return exist;
   }


   // user is already register
   public void isRegister(String email) {
      this.userValidator.emailValidate(email);

      boolean exist = this.userRepository.existsByEmail(email);
      if(exist) {
         throw MultiExceptions.duplicate(String.format(
            "%s: Email já cadastrado",
            ResponseMessages.DUPLICATE
         ));
      }
   }


   // update profile image id
   @Transactional
   public void updateProfileImageId(UUID userId, String profileImageId) {
      this.userValidator.idValidate(userId);
      this.profileImageValidator.idValidate(profileImageId);

      try {
         this.userRepository.updateProfileImageId(userId, profileImageId);
      }
      catch(NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe", 
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // remove profile image id
   @Transactional
   public void removeProfileImageId(UUID userId) {
      this.userValidator.idValidate(userId);

      try {
         this.userRepository.removeProfileImageId(userId);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe", 
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // update user
   @Transactional
   public UserProfileDTO update(UUID userId, UserUpdateDTO data) {
      this.userValidator.updateValidations(data);
      
      try {
         UserUpdateDTO finalData = data;

         // existing email - check
         if(Optional.ofNullable(data.email()).isPresent()) {
            UserProfileDTO currentUser = this.getById(userId); // userId will be validate here
            if(!currentUser.email().equals(data.email())) {
               this.isRegister(data.email());
            }
         }

         // password - check
         if(Optional.ofNullable(data.password()).isPresent()) {
            String encryptedPassword = this.passwordService.encryptPassword(data.password());
            finalData = data.withEncryptedPassword(encryptedPassword);
         }

         return this.userRepository.update(userId, finalData);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe", 
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // delete user
   @Transactional
   public UserDeletedDTO delete(UUID userId) {
      this.userValidator.idValidate(userId);

      try {
         return this.userRepository.delete(userId);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe", 
            ResponseMessages.NOT_FOUND
         ));
      }
   }


   // is deleted
   public boolean isDeleted(UUID userId) {
      this.userValidator.idValidate(userId);

      try {
         return this.userRepository.isDeleted(userId);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuário não existe", 
            ResponseMessages.NOT_FOUND
         ));
      }
   }

}