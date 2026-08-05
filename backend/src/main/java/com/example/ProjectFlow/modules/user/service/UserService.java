
// packages
package com.example.ProjectFlow.modules.user.service;

// imports
import org.springframework.stereotype.Service;
import java.util.List;

// jakart imports
import jakarta.persistence.NoResultException;

// import repository
import com.example.ProjectFlow.modules.user.repository.UserRepository;

// import validator
import com.example.ProjectFlow.modules.user.validator.UserValidator;
import com.example.ProjectFlow.modules.user.validator.ProfileImageValidator;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserDTO;
import com.example.ProjectFlow.modules.user.dto.UserProfileDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Service
public class UserService {

   // properties
   private final UserRepository userRepository;
   private final UserValidator userValidator;
   private final ProfileImageValidator profileImageValidator;

   // constructor - dependency injection
   public UserService(
      UserRepository userRepository,
      UserValidator userValidator,
      ProfileImageValidator profileImageValidator
   ) {
      this.userRepository = userRepository;
      this.userValidator = userValidator;
      this.profileImageValidator = profileImageValidator;
   }


   // get all users
   public List<UserProfileDTO> getAllUsers() {
      List<UserProfileDTO> users = this.userRepository.getAllUsers();

      if(users.isEmpty()) {
         throw MultiExceptions.notFound(String.format(
            "%s: Usuários não existem",
            ResponseMessages.NOT_FOUND
         ));
      }

      return users;
   }


   // get by id
   public UserProfileDTO getById(Long id) {
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
   public boolean existsById(Long id) {
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
   public void updateProfileImageId(Long userId, String profileImageId) {
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
   public void removeProfileImageId(Long userId) {
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

}