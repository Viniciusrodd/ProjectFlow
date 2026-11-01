
// packages
package com.example.ProjectFlow.modules.user.integrationTests.profileImageController;

// imports
import java.util.UUID;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 
// mockito imports
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.springframework.security.oauth2.jwt.JwtDecoder;
 
// spring imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 
// import controller
import com.example.ProjectFlow.modules.user.controller.ProfileImageController;
 
// import services
import com.example.ProjectFlow.modules.user.service.ProfileImageService;
 
// import mapper
import com.example.ProjectFlow.modules.user.mapper.ProfileImageMapper;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
 
@WebMvcTest(ProfileImageController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - ProfileImageController")
public class ProfileImageBadTests {

   // for reqs http simulation
   @Autowired
   private MockMvc mockMvc;
   
   // mock's for disable security filter in test
   @MockitoBean
   private JwtDecoder jwtDecoder;
   @MockitoBean
   private JwtFilter jwtFilter;
 
   // replaces real service and mapper for spring context mock
   @MockitoBean 
   private ProfileImageService profileImageService;
   @MockitoBean 
   private ProfileImageMapper profileImageMapper;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH + "/users/profile-image";
 
 
   @Test
   @DisplayName("POST - /users/profile-image/{userId} - should return 500 when image processing fails")
   void uploadProfileImage() throws Exception {
      // upload data
      UUID userId = UUID.randomUUID();
      MockMultipartFile file = new MockMultipartFile(
         "file", "profile.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes()
      );
 
      // error message
      String errorMessage = "Erro interno: Falha ao processar imagem de usuário";
 
      // stubs
      when(this.profileImageService.uploadProfileImage(any(UUID.class), any()))
         .thenThrow(MultiExceptions.internal(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         multipart(this.BASE_PATH + "/" + userId)
         .file(file)
      )
         .andExpect(status().isInternalServerError())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(500))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.profileImageService).uploadProfileImage(any(UUID.class), any());
   }


   @Test
   @DisplayName("GET - /users/profile-image/{userId} - should return 404 when profile image does not exist")
   void getProfileImage() throws Exception {
      // user id
      UUID userId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Imagem de perfil não existe";
 
      // stubs
      when(this.profileImageService.getProfileImage(userId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.profileImageService).getProfileImage(userId);
      
      // no checks
      verifyNoInteractions(this.profileImageMapper);
   }


   @Test
   @DisplayName("GET - /users/profile-image/{userId}/download - should return 404 when profile image does not exist")
   void getProfileImageData() throws Exception {
      // user id
      UUID userId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Imagem de perfil não existe";
 
      // stubs
      when(this.profileImageService.getProfileImage(userId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + userId + "/download")
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.profileImageService).getProfileImage(userId);
   }


   @Test
   @DisplayName("DELETE - /users/profile-image/{userId} - should return 404 when profile image does not exist")
   void deleteProfileImage() throws Exception {
      // user id
      UUID userId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Imagem de perfil não existe";
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage)).when(this.profileImageService).deleteProfileImage(userId);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.profileImageService).deleteProfileImage(userId);
   }

}