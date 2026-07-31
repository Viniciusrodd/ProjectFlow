
// packages
package com.example.ProjectFlow.modules.auth.service;

// imports
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

// import repository
import com.example.ProjectFlow.modules.auth.repository.AuthRepository;

// import validators
import com.example.ProjectFlow.modules.auth.validator.RegisterValidator;
import com.example.ProjectFlow.modules.auth.validator.LoginValidator;

// import security
import com.example.ProjectFlow.security.JWT.JwtService;

// import DTOs
import com.example.ProjectFlow.modules.auth.dto.RegisterDTO;
import com.example.ProjectFlow.modules.auth.dto.RegisterResponseDTO;


@Service
public class AuthService {
 
   // properties
   private final AuthRepository authRepository;
   private final PasswordService passwordService;
   private final RegisterValidator registerValidator;
   //private final LoginValidator loginValidator;
   //private final JwtService jwtService;

   // constructor - dependency injection
   public AuthService(
      AuthRepository authRepository,
      PasswordService passwordService,
      RegisterValidator registerValidator,
      LoginValidator loginValidator,
      JwtService jwtService
   ) {
      this.authRepository = authRepository;
      this.passwordService = passwordService;
      this.registerValidator = registerValidator;
      //this.loginValidator = loginValidator;
      //this.jwtService = jwtService;
   }


   // register
   @Transactional
   public RegisterResponseDTO register(RegisterDTO data) {
      // validation
      this.registerValidator.validate(data);

      // encrypt password
      final String encryptedPassword = this.passwordService.encryptPassword(data.password());

      // user data - setup
      RegisterDTO userData = new RegisterDTO(
         data.name(),
         data.email(),
         encryptedPassword
      );

      return this.authRepository.register(userData);
   }

}