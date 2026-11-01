
// packages
package com.example.ProjectFlow.modules.organization.integrationTests.organizationImageController;

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
import com.example.ProjectFlow.modules.organization.controller.OrganizationImageController;
 
// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationImageService;
 
// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationImageMapper;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
 
@WebMvcTest(OrganizationImageController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - OrganizationImageController")
public class OrganizationImageBadTests {
   
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
   private OrganizationImageService organizationImageService;
   @MockitoBean 
   private OrganizationImageMapper organizationImageMapper;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH + "/organization/logo-image";
 
 
   @Test
   @DisplayName("POST - /organization/logo-image/{organizationId}/user/{userId} - should return 500 when image processing fails")
   void uploadOrganizationImage() throws Exception {
      // upload data
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      MockMultipartFile file = new MockMultipartFile(
         "file", "logo.png", MediaType.IMAGE_PNG_VALUE, "fake-image-content".getBytes()
      );
 
      // error message
      String errorMessage = "Erro interno: Falha ao processar imagem da organização";
 
      // stubs
      when(this.organizationImageService.uploadOrganizationImage(eq(organizationId), eq(userId), any()))
         .thenThrow(MultiExceptions.internal(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         multipart(this.BASE_PATH + "/" + organizationId + "/user/" + userId)
         .file(file)
      )
         .andExpect(status().isInternalServerError())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(500))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationImageService).uploadOrganizationImage(eq(organizationId), eq(userId), any());
   }
 
 
   @Test
   @DisplayName("GET - /organization/logo-image/{organizationId} - should return 404 when organization image does not exist")
   void getOrganizationImage() throws Exception {
      // organization id
      UUID organizationId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Imagem de organização não existe";
 
      // stubs
      when(this.organizationImageService.getOrganizationImage(organizationId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + organizationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationImageService).getOrganizationImage(organizationId);
      
      // no checks
      verifyNoInteractions(this.organizationImageMapper);
   }
 
 
   @Test
   @DisplayName("GET - /organization/logo-image/{organizationId}/download - should return 404 when organization image does not exist")
   void getOrganizationImageData() throws Exception {
      // organization id
      UUID organizationId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Imagem de organização não existe";
 
      // stubs
      when(this.organizationImageService.getOrganizationImage(organizationId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + organizationId + "/download")
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationImageService).getOrganizationImage(organizationId);
   }
 
 
   @Test
   @DisplayName("DELETE - /organization/logo-image/{organizationId}/user/{userId} - should return 404 when organization image does not exist")
   void deleteOrganizationImage() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Imagem de organização não existe";
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage)).when(this.organizationImageService).deleteOrganizationImage(organizationId, userId);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/" + organizationId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.organizationImageService).deleteOrganizationImage(organizationId, userId);
   }
 
}