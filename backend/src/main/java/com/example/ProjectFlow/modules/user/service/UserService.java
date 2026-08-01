
// packages
package com.example.ProjectFlow.modules.user.service;

// imports
import org.springframework.stereotype.Service;

import jakarta.persistence.NoResultException;

// import repository
import com.example.ProjectFlow.modules.user.repository.UserRepository;

// import validator
import com.example.ProjectFlow.modules.user.validator.UserValidator;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;


@Service
public class UserService {

   // properties
   private final UserRepository userRepository;
   private final UserValidator userValidator;

   // constructor - dependency injection
   public UserService(
      UserRepository userRepository,
      UserValidator userValidator
   ) {
      this.userRepository = userRepository;
      this.userValidator = userValidator;
   }


   // find by email
   public UserDTO findByEmail(String email) {
      this.userValidator.emailValidate(email);

      try {
         return this.userRepository.findByEmail(email);
      }
      catch (NoResultException error) {
         throw MultiExceptions.notFound(ResponseMessages.NOT_FOUND);
      }
   }


   // exists by email
   public boolean existsByEmail(String email) {
      this.userValidator.emailValidate(email);

      return this.userRepository.existsByEmail(email);
   }

}