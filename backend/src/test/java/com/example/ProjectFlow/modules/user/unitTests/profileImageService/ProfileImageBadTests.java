
// packages
package com.example.ProjectFlow.modules.user.unitTests.profileImageService;

// imports
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// mockito imports
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// import document
import com.example.ProjectFlow.modules.user.document.ProfileImageDocument;

// import mapper
import com.example.ProjectFlow.modules.user.mapper.ProfileImageMapper;

// import repositories
import com.example.ProjectFlow.modules.user.repository.ProfileImageRepository;

// import services
import com.example.ProjectFlow.modules.user.service.ProfileImageService;
import com.example.ProjectFlow.modules.user.service.UserService;

// import validators
import com.example.ProjectFlow.modules.user.validator.ProfileImageValidator;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - ProfileImageService")
public class ProfileImageBadTests {
   
   // mock dependencies
   @Mock
   private ProfileImageRepository profileImageRepository;
   @Mock
   private UserService userService;
   @Mock
   private ProfileImageValidator profileImageValidator;
   @Mock
   private ProfileImageMapper profileImageMapper;

   // real instance for inject all mock objects above
   @InjectMocks  
   private ProfileImageService profileImageService;


   @Test
   @DisplayName("uploadProfileImage() - must fail in profile image processing")
   void uploadProfileImage() throws Exception {
      // data
      UUID userId = UUID.randomUUID();

      // file - mock
      MultipartFile file = mock(MultipartFile.class);
      when(file.getOriginalFilename()).thenReturn("filename.png");
      when(file.getContentType()).thenReturn("image/png");
      when(file.getSize()).thenReturn(3L);
      when(file.getBytes()).thenThrow(IOException.class);

      // mocks config
      when(this.userService.existsById(userId)).thenReturn(true);
      when(this.profileImageRepository.existsByUserId(userId)).thenReturn(false);

      // act and assert
      assertThatThrownBy(() -> this.profileImageService.uploadProfileImage(userId, file))
         .isInstanceOf(Exception.class)
         .hasMessageContaining("Erro interno do servidor: Falha ao processar imagem de usuário");

      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.profileImageValidator, times(1)).validate(file);
      verify(this.profileImageRepository, times(1)).existsByUserId(userId);

      // no checks
      verify(this.profileImageRepository, never()).save(any());
      verify(this.userService, never()).updateProfileImageId(any(), any());
      verifyNoInteractions(this.profileImageMapper);     
   }


   @Test 
   @DisplayName("getProfileImage() - must throw profile user image not found")
   void getProfileImage() {
      // data
      UUID userId = UUID.randomUUID();

      // mocks config
      when(this.userService.existsById(userId)).thenReturn(true);
      when(this.profileImageRepository.findByUserId(userId)).thenReturn(null);

      // act and assert
      assertThatThrownBy(() -> this.profileImageService.getProfileImage(userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Imagem de perfil não existe");

      // checks
      verify(this.userService, times(1)).existsById(userId);
      verify(this.profileImageRepository, times(1)).findByUserId(userId);
   }


   @Test 
   @DisplayName("getAllProfileImages() - must throw profile images not found")
   void getAllProfileImages() {
      // list - setup
      List<ProfileImageDocument> documents = new ArrayList<>();

      // mocks config
      when(this.profileImageRepository.findAll()).thenReturn(documents);

      // act and assert
      assertThatThrownBy(() -> this.profileImageService.getAllProfileImages())
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Imagens de perfil não existem");

      // checks
      verify(this.profileImageRepository, times(1)).findAll();
   }

}