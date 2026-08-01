
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
import com.example.ProjectFlow.modules.auth.dto.LoginDTO;
import com.example.ProjectFlow.modules.auth.dto.LoginResponseDTO;
import com.example.ProjectFlow.modules.user.dto.UserDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;

// import services
import com.example.ProjectFlow.modules.user.service.UserService;


@Service
public class AuthService {
 
   // properties
   private final AuthRepository authRepository;
   private final PasswordService passwordService;
   private final RegisterValidator registerValidator;
   private final LoginValidator loginValidator;
   private final JwtService jwtService;
   private final UserService userService;

   // constructor - dependency injection
   public AuthService(
      AuthRepository authRepository,
      PasswordService passwordService,
      RegisterValidator registerValidator,
      LoginValidator loginValidator,
      JwtService jwtService,
      UserService userService
   ) {
      this.authRepository = authRepository;
      this.passwordService = passwordService;
      this.registerValidator = registerValidator;
      this.loginValidator = loginValidator;
      this.jwtService = jwtService;
      this.userService = userService;
   }


   // register
   @Transactional
   public RegisterResponseDTO register(RegisterDTO data) {
      // validation
      this.registerValidator.validate(data);

      // email already exists - check
      if(this.userService.existsByEmail(data.email())) {
         throw MultiExceptions.duplicate(String.format(
            "%s: Email já cadastrado",
            ResponseMessages.DUPLICATE
         ));
      }

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


   // login
   public LoginResponseDTO login(LoginDTO data) {
      // validation
      this.loginValidator.validate(data);

      // find user by email
      UserDTO user = this.userService.findByEmail(data.email());

      // password matches - check
      if(!this.passwordService.matches(data.password(), user.password())) {
         throw MultiExceptions.unauthorized(String.format(
            "%s: Senha incorreta",
            ResponseMessages.UNAUTHORIZED
         ));
      }

      // JWT token - generation
      String token = this.jwtService.generateToken(user);

      return new LoginResponseDTO(
         token,
         user.email(),
         user.name(),
         user.id()
      );
   }

}