
// packages
package com.example.ProjectFlow.modules.auth.unitTests.authService;

// imports
import java.util.UUID;

// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

// mockito imports
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// import security
import com.example.ProjectFlow.security.JWT.JwtService;
import com.example.ProjectFlow.exception.MultiExceptions;
// import DTOs
import com.example.ProjectFlow.modules.auth.dto.loginDTO.LoginDTO;
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterDTO;
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
@DisplayName("Unit tests (Bad path) - AuthService")
public class AuthBadTests {
 
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
   @DisplayName("register() - must throw when user is already register")
   void register() {
      // error
      String errorMessage = "Recurso já existe: Email já cadastrado";

      // user - setup
      UserEntity user = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Vini")
         .email("vini@gmail.com")
         .password("vini123")
         .build();

      // dto - setup
      RegisterDTO dto = new RegisterDTO(user.getName(), user.getEmail(), user.getPassword());

      // mocks config
      doThrow(MultiExceptions.duplicate(errorMessage))
         .when(this.userService).isRegister(user.getEmail());

      // act and assert
      assertThatThrownBy(() -> this.authService.register(dto))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining(errorMessage);
      
      // checks
      verify(this.registerValidator, times(1)).nameValidate(user.getName());
      verify(this.registerValidator, times(1)).emailValidate(user.getEmail());
      verify(this.registerValidator, times(1)).passwordValidate(user.getPassword());
      verify(this.userService, times(1)).isRegister(user.getEmail());
      
      // no checks
      verifyNoInteractions(this.passwordService);
      verifyNoInteractions(this.authRepository);
      verifyNoInteractions(this.authMapper);
   }


   @Test
   @DisplayName("login() - must throw an error when user password is wrong")
   void login() {
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

      // mocks config
      when(this.userService.getByEmail(user.getEmail())).thenReturn(userDTO);
      when(this.passwordService.matches(loginDTO.password(), userDTO.password())).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.authService.login(loginDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Não autorizado: Senha incorreta");

      // checks
      verify(this.loginValidator, times(1)).emailValidate(user.getEmail());
      verify(this.loginValidator, times(1)).passwordValidate(user.getPassword());
      verify(this.userService, times(1)).getByEmail(user.getEmail());
      verify(this.passwordService, times(1)).matches(loginDTO.password(), userDTO.password());

      // no checks
      verifyNoInteractions(this.jwtService);
      verifyNoInteractions(this.authMapper);      
   }

}