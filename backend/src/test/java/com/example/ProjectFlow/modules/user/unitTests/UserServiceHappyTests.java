
// packages
package com.example.ProjectFlow.modules.user.unitTests;

// imports
import java.time.LocalDateTime;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.userDTO.UserDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserDeletedDTO;
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


@ExtendWith(MockitoExtension.class) // enable Mockito in JUnit 5
@DisplayName("Unit tests (Happy path) - UserService")
public class UserServiceHappyTests {
 
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
   @DisplayName("getAll() - must return all users")
   void getAll() {
      // user 1 - setup
      UserEntity user1 = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Vini")
         .email("vini@gmail.com")
         .build();
      UserProfileDTO dto1 = new UserProfileDTO(user1.getId(), user1.getEmail(), user1.getName(), null); 

      // user 2 - setup
      UserEntity user2 = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Maria")
         .email("maria@gmail.com")
         .build();
      UserProfileDTO dto2 = new UserProfileDTO(user2.getId(), user2.getEmail(), user2.getName(), null); 

      // list - setup
      List<UserEntity> users = List.of(user1, user2);

      // mocks config
      when(this.userRepository.getAll()).thenReturn(users);
      when(this.userMapper.toUserProfileDTO(user1)).thenReturn(dto1);
      when(this.userMapper.toUserProfileDTO(user2)).thenReturn(dto2);

      // act and assert
      List<UserProfileDTO> result = this.usersService.getAll();
      assertThat(result)
         .isNotNull()
         .hasSize(2)
         .containsExactlyInAnyOrder(dto1, dto2);

      // checks
      verify(this.userRepository, times(1)).getAll(); // check if userRepo. was call at least 1 time
      verify(this.userMapper, times(1)).toUserProfileDTO(user1);
      verify(this.userMapper, times(1)).toUserProfileDTO(user2);
   }


   @Test 
   @DisplayName("getById() - must return user by id")
   void getById() {
      // user 1 - setup
      UserEntity user = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Vini")
         .email("vini@gmail.com")
         .build();
      UserProfileDTO dto = new UserProfileDTO(user.getId(), user.getEmail(), user.getName(), null);

      // mocks config
      when(this.userRepository.getById(user.getId())).thenReturn(user);
      when(this.userMapper.toUserProfileDTO(user)).thenReturn(dto);

      // act and assert
      UserProfileDTO result = this.usersService.getById(user.getId());
      assertThat(result)
         .isNotNull()
         .isEqualTo(dto);
      
      // checks
      verify(this.userValidator, times(1)).idValidate(user.getId());
      verify(this.userRepository, times(1)).getById(user.getId());
      verify(this.userMapper, times(1)).toUserProfileDTO(user);
   }


   @Test 
   @DisplayName("getByEmail() - must return user by email")
   void getByEmail() {
      // user 1 - setup
      UserEntity user = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Vini")
         .email("vini@gmail.com")
         .password("vini123")
         .build();
      UserDTO dto = new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getPassword());

      // mocks config
      when(this.userRepository.getByEmail(user.getEmail())).thenReturn(user);
      when(this.userMapper.toUserDTO(user)).thenReturn(dto);

      // act and assert
      UserDTO result = this.usersService.getByEmail(user.getEmail());
      assertThat(result)
         .isNotNull()
         .isEqualTo(dto);
      
      // checks
      verify(this.userValidator, times(1)).emailValidate(user.getEmail());
      verify(this.userRepository, times(1)).getByEmail(user.getEmail());
      verify(this.userMapper, times(1)).toUserDTO(user);
   }


   @Test 
   @DisplayName("getEntityById() - must return user entity by id")
   void getEntityById() {
      // user 1 - setup
      UserEntity user = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // mocks config
      when(this.userRepository.getEntityById(user.getId())).thenReturn(user);

      // act and assert
      UserEntity result = this.usersService.getEntityById(user.getId());
      assertThat(result)
         .isNotNull()
         .isEqualTo(user);
      
      // checks
      verify(this.userValidator, times(1)).idValidate(user.getId());
      verify(this.userRepository, times(1)).getEntityById(user.getId());
   }


   @Test 
   @DisplayName("existsById() - must check if user exist by id")
   void existsById() {
      // user 1 - setup
      UserEntity user = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // mocks config
      when(this.userRepository.existsById(user.getId())).thenReturn(true);

      // act and assert
      boolean result = this.usersService.existsById(user.getId());
      assertThat(result).isTrue();
      
      // checks
      verify(this.userValidator, times(1)).idValidate(user.getId());
      verify(this.userRepository, times(1)).existsById(user.getId());
   }


   @Test 
   @DisplayName("update() - must update user (only the name)")
   void update() {
      // user 1 - setup
      UserEntity user = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Novo Nome")
         .email("vini@gmail.com")
         .build();
      
      // DTOs - setup
      UserUpdateDTO dataToUpdate = new UserUpdateDTO("Novo Nome", null, null);
      UserProfileDTO expectedDto = new UserProfileDTO(user.getId(), user.getEmail(), dataToUpdate.name(), null);

      // mocks config
      when(this.userRepository.update(user.getId(), dataToUpdate)).thenReturn(user);
      when(this.userMapper.toUserProfileDTO(user)).thenReturn(expectedDto);

      // act and assert
      UserProfileDTO result = this.usersService.update(user.getId(), dataToUpdate);
      assertThat(result)
         .isNotNull()
         .isEqualTo(expectedDto);
      
      // checks
      verify(this.userValidator, times(1)).updateValidations(dataToUpdate);
      verify(this.userRepository, never()).getById(any(UUID.class));
      verify(this.userRepository, times(1)).update(user.getId(), dataToUpdate);
      verify(this.userMapper, times(1)).toUserProfileDTO(user);
      
      // no checks
      verifyNoInteractions(this.passwordService);
      verify(this.userRepository, never()).existsByEmail(anyString());
   }


   @Test 
   @DisplayName("delete() - must delete user")
   void delete() {
      // user 1 - setup
      UserEntity user = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .name("Vini")
         .email("vini@gmail.com")
         .build();
      UserDeletedDTO dto = new UserDeletedDTO(user.getId(), user.getName(), user.getEmail(), LocalDateTime.now());

      // mocks config
      when(this.userRepository.delete(user.getId())).thenReturn(user);
      when(this.userMapper.toUserDeletedDTO(user)).thenReturn(dto);

      // act and assert
      UserDeletedDTO result = this.usersService.delete(user.getId());
      assertThat(result)
         .isNotNull()
         .isEqualTo(dto);
      
      // checks
      verify(this.userValidator, times(1)).idValidate(user.getId());
      verify(this.userRepository, times(1)).delete(user.getId());
      verify(this.userMapper, times(1)).toUserDeletedDTO(user);
   }


   @Test 
   @DisplayName("isDeleted() - must check if user is deleted")
   void isDeleted() {
      // user 1 - setup
      UserEntity user = new UserEntity.Builder()
         .id(UUID.randomUUID())
         .build();

      // mocks config
      when(this.userRepository.isDeleted(user.getId())).thenReturn(true);

      // act and assert
      boolean result = this.usersService.isDeleted(user.getId());
      assertThat(result).isTrue();
      
      // checks
      verify(this.userValidator, times(1)).idValidate(user.getId());
      verify(this.userRepository, times(1)).isDeleted(user.getId());
   }

}