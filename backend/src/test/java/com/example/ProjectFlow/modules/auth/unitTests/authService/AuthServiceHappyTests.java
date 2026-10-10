
// packages
package com.example.ProjectFlow.modules.auth.unitTests.authService;

// imports
import java.time.LocalDateTime;
import java.util.UUID;

// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

// mockito imports
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// import security
import com.example.ProjectFlow.security.JWT.JwtService;

// import DTOs
import com.example.ProjectFlow.modules.auth.dto.loginDTO.LoginDTO;
import com.example.ProjectFlow.modules.auth.dto.loginDTO.LoginResponseDTO;
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterDTO;
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterResponseDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserDTO;

// import entities
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import mapper
import com.example.ProjectFlow.modules.auth.mapper.AuthMapper;

// import repositories
import com.example.ProjectFlow.modules.auth.repository.AuthRepository;

// import services
import com.example.ProjectFlow.modules.auth.service.AuthService;
import com.example.ProjectFlow.modules.auth.service.PasswordService;
import com.example.ProjectFlow.modules.user.service.UserService;

// import validators
import com.example.ProjectFlow.modules.auth.validator.RegisterValidator;
import com.example.ProjectFlow.modules.auth.validator.LoginValidator;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - AuthService")
public class AuthServiceHappyTests {
 
   // mock dependencies
   @Mock
   private AuthRepository authRepository;
   @Mock
   private PasswordService passwordService;
   @Mock
   private JwtService jwtService;
   @Mock
   private UserService userService;
   @Mock
   private RegisterValidator registerValidator;
   @Mock
   private LoginValidator loginValidator;
   @Mock
   private AuthMapper authMapper;

   // real instance for inject all mock objects above
   @InjectMocks  
   private AuthService authService;


   @Test
   @DisplayName("register() - must create a user")
   void register() {
      // password - setup
      String rawPassword = "vini123";
      String encryptedPassword = "dknsaldsdsa";

      // user - setup
      UserEntity user = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Vini")
         .email("vini@gmail.com")
         .password(encryptedPassword)
         .build();

      // dto - setup
      RegisterDTO dto = new RegisterDTO(user.getName(), user.getEmail(), rawPassword);
      RegisterDTO userData = new RegisterDTO(user.getName(), user.getEmail(), encryptedPassword);
      RegisterResponseDTO expectedDto = new RegisterResponseDTO(user.getId(), user.getName(), user.getEmail(), LocalDateTime.now());

      // mocks config
      // userService.isRegister() -> null
      when(this.passwordService.encryptPassword(rawPassword)).thenReturn(encryptedPassword);
      when(this.authRepository.register(userData)).thenReturn(user);
      when(this.authMapper.toRegisterResponseDTO(user)).thenReturn(expectedDto);

      // act and assert
      RegisterResponseDTO result = this.authService.register(dto);
      assertThat(result)
         .isNotNull()
         .isEqualTo(expectedDto);
      
      // checks
      verify(this.registerValidator, times(1)).nameValidate(user.getName());
      verify(this.registerValidator, times(1)).emailValidate(user.getEmail());
      verify(this.registerValidator, times(1)).passwordValidate(rawPassword);
      verify(this.userService, times(1)).isRegister(user.getEmail());
      verify(this.passwordService, times(1)).encryptPassword(rawPassword);
      verify(this.authRepository, times(1)).register(userData);
      verify(this.authMapper, times(1)).toRegisterResponseDTO(user);
   }


   @Test
   @DisplayName("login() - must allow a user login")
   void login() {
      // data
      String token = "dasdasdsadas";
      
      // user - setup
      UserEntity user = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Vini")
         .email("vini@gmail.com")
         .password("vini123")
         .build();

      // dto - setup
      UserDTO userDTO = new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getPassword());
      LoginDTO loginDTO = new LoginDTO(user.getEmail(), user.getPassword());
      LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), user.getName(), user.getEmail(), token);

      // mocks config
      when(this.userService.getByEmail(user.getEmail())).thenReturn(userDTO);
      when(this.passwordService.matches(loginDTO.password(), userDTO.password())).thenReturn(true);
      when(this.jwtService.generateToken(userDTO)).thenReturn(token);
      when(this.authMapper.toLoginResponseDTO(userDTO, token)).thenReturn(loginResponseDTO);

      // act and assert
      LoginResponseDTO result = this.authService.login(loginDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(loginResponseDTO);

      // checks
      verify(this.loginValidator, times(1)).emailValidate(user.getEmail());
      verify(this.loginValidator, times(1)).passwordValidate(user.getPassword());
      verify(this.userService, times(1)).getByEmail(user.getEmail());
      verify(this.passwordService, times(1)).matches(loginDTO.password(), userDTO.password());
      verify(this.jwtService, times(1)).generateToken(userDTO);
      verify(this.authMapper, times(1)).toLoginResponseDTO(userDTO, token);      
   }

}