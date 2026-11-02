
// packages
package com.example.ProjectFlow.modules.project.integrationTests.projectImageController;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 
// mockito imports
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.example.ProjectFlow.modules.project.controller.ProjectImageController;
 
// import services
import com.example.ProjectFlow.modules.project.service.ProjectImageService;
 
// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectImageMapper;
 
// import document
import com.example.ProjectFlow.modules.project.document.ProjectImageDocument;
 
// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectImageDTO.ProjectImageResponseDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
 
@WebMvcTest(ProjectImageController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - ProjectImageController")
public class ProjectImageHappyTests {
   
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
   private ProjectImageService projectImageService;
   @MockitoBean 
   private ProjectImageMapper projectImageMapper;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH + "/project/logo-image";
   
   
   @Test
   @DisplayName("POST - /project/logo-image/{projectId}/user/{userId} - should return the uploaded image data and status 201")
   void uploadProjectImage() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      byte[] imageBytes = "fake-image-content".getBytes();
      MockMultipartFile file = new MockMultipartFile(
         "file", "logo.png", MediaType.IMAGE_PNG_VALUE, imageBytes
      );
 
      // upload response data
      ProjectImageResponseDTO projectImageResponseDTO = new ProjectImageResponseDTO(
         "projectImage123", projectId, "logo.png", 
         MediaType.IMAGE_PNG_VALUE, (long) imageBytes.length, LocalDateTime.now()
      );
 
      // stubs
      when(this.projectImageService.uploadProjectImage(eq(projectId), eq(userId), any()))
         .thenReturn(projectImageResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         multipart(this.BASE_PATH + "/" + projectId + "/user/" + userId)
         .file(file)
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(projectImageResponseDTO.id()))
         .andExpect(jsonPath("$.data.projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data.fileName").value(projectImageResponseDTO.fileName()))
         .andExpect(jsonPath("$.data.mimeType").value(projectImageResponseDTO.mimeType()))
         .andExpect(jsonPath("$.data.size").value(projectImageResponseDTO.size()))
         .andExpect(jsonPath("$.data.uploadDate").isNotEmpty());
 
      // checks
      verify(this.projectImageService).uploadProjectImage(eq(projectId), eq(userId), any());
   }
 
 
   @Test
   @DisplayName("GET - /project/logo-image/{projectId} - should return the image data and status 200")
   void getProjectImage() throws Exception {
      // project image data
      UUID projectId = UUID.randomUUID();
      ProjectImageDocument document = new ProjectImageDocument.Builder()
         .projectId(projectId)
         .fileName("logo.png")
         .mimeType(MediaType.IMAGE_PNG_VALUE)
         .size(18L)
         .uploadDate(LocalDateTime.now())
         .binary("fake-image-content".getBytes())
         .build();
 
      // image response data
      ProjectImageResponseDTO projectImageResponseDTO = new ProjectImageResponseDTO(
         "projectImage123", projectId, "logo.png", 
         MediaType.IMAGE_PNG_VALUE, 18L, LocalDateTime.now()
      );
 
      // stubs
      when(this.projectImageService.getProjectImage(projectId)).thenReturn(document);
      when(this.projectImageMapper.toProjectImageResponseDTO(document)).thenReturn(projectImageResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + projectId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(projectImageResponseDTO.id()))
         .andExpect(jsonPath("$.data.projectId").value(projectId.toString()))
         .andExpect(jsonPath("$.data.fileName").value(projectImageResponseDTO.fileName()))
         .andExpect(jsonPath("$.data.mimeType").value(projectImageResponseDTO.mimeType()))
         .andExpect(jsonPath("$.data.size").value(projectImageResponseDTO.size()));
 
      // checks
      verify(this.projectImageService).getProjectImage(projectId);
      verify(this.projectImageMapper).toProjectImageResponseDTO(document);
   }
 
 
   @Test
   @DisplayName("GET - /project/logo-image/{projectId}/download - should return the image bytes and status 200")
   void getProjectImageData() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      byte[] imageBytes = "fake-image-content".getBytes();
      ProjectImageDocument document = new ProjectImageDocument.Builder()
         .projectId(projectId)
         .fileName("logo.png")
         .mimeType(MediaType.IMAGE_PNG_VALUE)
         .size((long) imageBytes.length)
         .uploadDate(LocalDateTime.now())
         .binary(imageBytes)
         .build();
 
      // stubs
      when(this.projectImageService.getProjectImage(projectId)).thenReturn(document);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + projectId + "/download")
         .accept(MediaType.IMAGE_PNG)
      )
         .andExpect(status().isOk())
         .andExpect(content().contentType(MediaType.IMAGE_PNG))
         .andExpect(header().string("Content-Disposition", "inline; filename=\"logo.png\""))
         .andExpect(content().bytes(imageBytes));
 
      // checks
      verify(this.projectImageService).getProjectImage(projectId);
   }
 
 
   @Test
   @DisplayName("DELETE - /project/logo-image/{projectId}/user/{userId} - should return status 200")
   void deleteProjectImage() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/" + projectId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // checks
      verify(this.projectImageService).deleteProjectImage(projectId, userId);
   }

}