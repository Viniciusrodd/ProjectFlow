
// packages
package com.example.ProjectFlow.modules.user.integrationTests.profileImageController;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 
// mockito imports
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 
// import controller
import com.example.ProjectFlow.modules.user.controller.ProfileImageController;
 
// import services
import com.example.ProjectFlow.modules.user.service.ProfileImageService;
 
// import mapper
import com.example.ProjectFlow.modules.user.mapper.ProfileImageMapper;
 
// import document
import com.example.ProjectFlow.modules.user.document.ProfileImageDocument;
 
// import DTOs
import com.example.ProjectFlow.modules.user.dto.profileImageDTO.ProfileImageResponseDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
 
@WebMvcTest(ProfileImageController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - ProfileImageController")
public class ProfileImageHappyTests {
 
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
   @DisplayName("POST - /users/profile-image/{userId} - should return the uploaded image data and status 201")
   void uploadProfileImage() throws Exception {
      // data - setup
      UUID userId = UUID.randomUUID();
      byte[] imageBytes = "fake-image-content".getBytes();
      MockMultipartFile file = new MockMultipartFile(
         "file", "profile.png", MediaType.IMAGE_PNG_VALUE, imageBytes
      );
 
      // upload response data
      ProfileImageResponseDTO profileImageResponseDTO = new ProfileImageResponseDTO(
         "profileImage123", userId, "profile.png", 
         MediaType.IMAGE_PNG_VALUE, (long) imageBytes.length, LocalDateTime.now()
      );
 
      // stubs
      when(this.profileImageService.uploadProfileImage(any(UUID.class), any())).thenReturn(profileImageResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         multipart(this.BASE_PATH + "/" + userId)
         .file(file)
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(profileImageResponseDTO.id()))
         .andExpect(jsonPath("$.data.userId").value(userId.toString()))
         .andExpect(jsonPath("$.data.fileName").value(profileImageResponseDTO.fileName()))
         .andExpect(jsonPath("$.data.mimeType").value(profileImageResponseDTO.mimeType()))
         .andExpect(jsonPath("$.data.size").value(profileImageResponseDTO.size()))
         .andExpect(jsonPath("$.data.uploadDate").isNotEmpty());
 
      // checks
      verify(this.profileImageService).uploadProfileImage(any(UUID.class), any());
   }
 
 
   @Test
   @DisplayName("GET - /users/profile-image/{userId} - should return the image data and status 200")
   void getProfileImage() throws Exception {
      // profile image data
      UUID userId = UUID.randomUUID();
      ProfileImageDocument document = new ProfileImageDocument.Builder()
         .userId(userId)
         .fileName("profile.png")
         .mimeType(MediaType.IMAGE_PNG_VALUE)
         .size(18L)
         .uploadDate(LocalDateTime.now())
         .binary("fake-image-content".getBytes())
         .build();
 
      // image response data
      ProfileImageResponseDTO profileImageResponseDTO = new ProfileImageResponseDTO(
         "profileImage123", userId, "profile.png", 
         MediaType.IMAGE_PNG_VALUE, 18L, LocalDateTime.now()
      );
 
      // stubs
      when(this.profileImageService.getProfileImage(userId)).thenReturn(document);
      when(this.profileImageMapper.toProfileImageResponseDTO(document)).thenReturn(profileImageResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(profileImageResponseDTO.id()))
         .andExpect(jsonPath("$.data.userId").value(userId.toString()))
         .andExpect(jsonPath("$.data.fileName").value(profileImageResponseDTO.fileName()))
         .andExpect(jsonPath("$.data.mimeType").value(profileImageResponseDTO.mimeType()))
         .andExpect(jsonPath("$.data.size").value(profileImageResponseDTO.size()));
 
      // checks
      verify(this.profileImageService).getProfileImage(userId);
      verify(this.profileImageMapper).toProfileImageResponseDTO(document);
   }
 
 
   @Test
   @DisplayName("GET - /users/profile-image/{userId}/download - should return the image bytes and status 200")
   void getProfileImageData() throws Exception {
      // data - setup
      UUID userId = UUID.randomUUID();
      byte[] imageBytes = "fake-image-content".getBytes();
      ProfileImageDocument document = new ProfileImageDocument.Builder()
         .userId(userId)
         .fileName("profile.png")
         .mimeType(MediaType.IMAGE_PNG_VALUE)
         .size((long) imageBytes.length)
         .uploadDate(LocalDateTime.now())
         .binary(imageBytes)
         .build();
 
      // stubs
      when(this.profileImageService.getProfileImage(userId)).thenReturn(document);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + userId + "/download")
         .accept(MediaType.IMAGE_PNG)
      )
         .andExpect(status().isOk())
         .andExpect(content().contentType(MediaType.IMAGE_PNG))
         .andExpect(header().string("Content-Disposition", "inline; filename=\"profile.png\""))
         .andExpect(content().bytes(imageBytes));
 
      // checks
      verify(this.profileImageService).getProfileImage(userId);
   }
 
 
   @Test
   @DisplayName("DELETE - /users/profile-image/{userId} - should return status 200")
   void deleteProfileImage() throws Exception {
      // user id
      UUID userId = UUID.randomUUID();
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // checks
      verify(this.profileImageService).deleteProfileImage(userId);
   }

}