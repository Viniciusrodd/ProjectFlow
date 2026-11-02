
// packages
package com.example.ProjectFlow.modules.project.integrationTests.projectImageController;

// imports
import java.util.UUID;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 
// mockito imports
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.example.ProjectFlow.modules.project.controller.ProjectImageController;
 
// import services
import com.example.ProjectFlow.modules.project.service.ProjectImageService;
 
// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectImageMapper;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
 
@WebMvcTest(ProjectImageController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - ProjectImageController")
public class ProjectImageBadTests {

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
   @DisplayName("POST - /project/logo-image/{projectId}/user/{userId} - should return 500 when image processing fails")
   void uploadProjectImage() throws Exception {
      // upload data
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      MockMultipartFile file = new MockMultipartFile(
         "file", "logo.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes()
      );
 
      // error message
      String errorMessage = "Erro interno: Falha ao processar imagem do projeto";
 
      // stubs
      when(this.projectImageService.uploadProjectImage(eq(projectId), eq(userId), any()))
         .thenThrow(MultiExceptions.internal(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         multipart(this.BASE_PATH + "/" + projectId + "/user/" + userId)
         .file(file)
      )
         .andExpect(status().isInternalServerError())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(500))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectImageService).uploadProjectImage(eq(projectId), eq(userId), any());
   }
 
 
   @Test
   @DisplayName("GET - /project/logo-image/{projectId} - should return 404 when project image does not exist")
   void getProjectImage() throws Exception {
      // project id
      UUID projectId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Imagem de projeto não existe";
 
      // stubs
      when(this.projectImageService.getProjectImage(projectId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + projectId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectImageService).getProjectImage(projectId);
      
      // no checks
      verifyNoInteractions(this.projectImageMapper);
   }
 
 
   @Test
   @DisplayName("GET - /project/logo-image/{projectId}/download - should return 404 when project image does not exist")
   void getProjectImageData() throws Exception {
      // project id
      UUID projectId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Imagem de projeto não existe";
 
      // stubs
      when(this.projectImageService.getProjectImage(projectId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + projectId + "/download")
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectImageService).getProjectImage(projectId);
   }
 
 
   @Test
   @DisplayName("DELETE - /project/logo-image/{projectId}/user/{userId} - should return 404 when project image does not exist")
   void deleteProjectImage() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Imagem de projeto não existe";
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage)).when(this.projectImageService).deleteProjectImage(projectId, userId);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/" + projectId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectImageService).deleteProjectImage(projectId, userId);
   }
 

}