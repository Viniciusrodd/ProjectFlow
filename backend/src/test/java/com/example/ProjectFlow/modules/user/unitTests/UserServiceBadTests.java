
// packages
package com.example.ProjectFlow.modules.user.unitTests;

// imports
import java.util.List;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserUpdateDTO;

// import entities
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import mapper
import com.example.ProjectFlow.modules.user.mapper.UserMapper;

// import repositories
import com.example.ProjectFlow.modules.user.repository.UserRepository;

// import services
import com.example.ProjectFlow.modules.auth.service.PasswordService;
import com.example.ProjectFlow.modules.user.service.UserService;

// import validators
import com.example.ProjectFlow.modules.user.validator.ProfileImageValidator;
import com.example.ProjectFlow.modules.user.validator.UserValidator;

// jakart imports
import jakarta.persistence.NoResultException;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - UserService")
public class UserServiceBadTests {
 
   // mock dependencies
   @Mock
   private UserRepository userRepository;
   @Mock
   private UserValidator userValidator;
   @Mock
   private ProfileImageValidator profileImageValidator;
   @Mock
   private UserMapper userMapper;
   @Mock
   private PasswordService passwordService;
   
   // real instance for inject all mock objects above
   @InjectMocks  
   private UserService usersService;


   @Test
   @DisplayName("getAll() - must return a empty user list")
   void getAll() {
      // list - setup
      List<UserEntity> users = List.of();

      // mocks config
      when(this.userRepository.getAll()).thenReturn(users);

      // act and assert
      assertThatThrownBy(() -> this.usersService.getAll())
         .isInstanceOf(RuntimeException.class) // MultiExceptions -> BaseException -> RuntimeException
         .hasMessageContaining("Recurso não encontrado: Usuários não existem");

      // checks
      verify(this.userRepository, times(1)).getAll();
      
      // no checks
      verifyNoInteractions(this.userMapper);
   }


   @Test 
   @DisplayName("getById() - must throw user not found")
   void getById() {
      UUID userId = UUID.randomUUID();
      
      // mocks config
      when(this.userRepository.getById(userId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.usersService.getById(userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Usuário não existe");

      // checks
      verify(this.userValidator, times(1)).idValidate(userId);
      verify(this.userRepository, times(1)).getById(userId);
      
      // no checks
      verifyNoInteractions(this.userMapper);
   }


   @Test 
   @DisplayName("getByEmail() - must throw user not found")
   void getByEmail() {
      // data
      String userEmail = "vini@gmail.com";

      // mocks config
      when(this.userRepository.getByEmail(userEmail)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.usersService.getByEmail(userEmail))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Usuário não existe");
      
      // checks
      verify(this.userValidator, times(1)).emailValidate(userEmail);
      verify(this.userRepository, times(1)).getByEmail(userEmail);
      
      // no checks
      verifyNoInteractions(this.userMapper);
   }


   @Test 
   @DisplayName("getEntityById() - must throw user not found")
   void getEntityById() {
      // data
      UUID userId = UUID.randomUUID();
      
      // mocks config
      when(this.userRepository.getEntityById(userId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.usersService.getEntityById(userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Usuário não existe");

      // checks
      verify(this.userValidator, times(1)).idValidate(userId);
      verify(this.userRepository, times(1)).getEntityById(userId);
   }


   @Test 
   @DisplayName("existsById() - must throw user not exist")
   void existsById() {
      // data
      UUID userId = UUID.randomUUID();

      // mocks config
      when(this.userRepository.existsById(userId)).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.usersService.existsById(userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Usuário não existe");
      
      // checks
      verify(this.userValidator, times(1)).idValidate(userId);
      verify(this.userRepository, times(1)).existsById(userId);
   }


   @Test 
   @DisplayName("update() - must throw user is already register by email")
   void update() {
      // data
      UUID userId = UUID.randomUUID();
      String newEmail = "novo@gmail.com";
      UserEntity user = new UserEntity.Builder()
         .id(userId)
         .name("vini")
         .email("vini@gmail.com")
         .build();
      
      // DTOs - setup
      UserUpdateDTO dataToUpdate = new UserUpdateDTO(null, newEmail, null);
      UserProfileDTO expectedDto = new UserProfileDTO(userId, user.getEmail(), user.getName(), null);

      // mocks config
      when(this.userRepository.getById(userId)).thenReturn(user);
      when(this.userMapper.toUserProfileDTO(user)).thenReturn(expectedDto);
      when(this.userRepository.existsByEmail(newEmail)).thenReturn(true);

      // act and assert
      assertThatThrownBy(() -> this.usersService.update(userId, dataToUpdate))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso já existe: Email já cadastrado");
      
      // checks
      verify(this.userValidator, times(1)).updateValidations(dataToUpdate);
      verify(this.userRepository, times(1)).getById(userId);
      verify(this.userMapper, times(1)).toUserProfileDTO(user);
      verify(this.userRepository, times(1)).existsByEmail(newEmail);

      // no checks
      verifyNoInteractions(this.passwordService);
      verify(this.userRepository, never()).update(any(UUID.class), any());
   }


   @Test 
   @DisplayName("delete() - must throw user not exist")
   void delete() {
      // data
      UUID userId = UUID.randomUUID();

      // mocks config
      when(this.userRepository.delete(userId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.usersService.delete(userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Usuário não existe");
      
      // checks
      verify(this.userValidator, times(1)).idValidate(userId);
      verify(this.userRepository, times(1)).delete(userId);

      // no checks
      verifyNoInteractions(userMapper);
   }


   @Test 
   @DisplayName("isDeleted() - must throw user not exist")
   void isDeleted() {
      // data
      UUID userId = UUID.randomUUID();

      // mocks config
      when(this.userRepository.isDeleted(userId)).thenThrow(NoResultException.class);

      // act and assert
      assertThatThrownBy(() -> this.usersService.isDeleted(userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Usuário não existe");
      
      // checks
      verify(this.userValidator, times(1)).idValidate(userId);
      verify(this.userRepository, times(1)).isDeleted(userId);
   }

}