
// packages
package com.example.ProjectFlow.modules.organization.integrationTests.organizationImageController;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 
// mockito imports
import static org.mockito.ArgumentMatchers.eq;
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
import com.example.ProjectFlow.modules.organization.controller.OrganizationImageController;
 
// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationImageService;
 
// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationImageMapper;
 
// import document
import com.example.ProjectFlow.modules.organization.document.OrganizationImageDocument;
 
// import DTOs
import com.example.ProjectFlow.modules.organization.dto.organizationImageDTO.OrganizationImageResponseDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
 
@WebMvcTest(OrganizationImageController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - OrganizationImageController")
public class OrganizationImageHappyTests {
   
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
   @DisplayName("POST - /organization/logo-image/{organizationId}/user/{userId} - should return the uploaded image data and status 201")
   void uploadOrganizationImage() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      byte[] imageBytes = "fake-image-content".getBytes();
      MockMultipartFile file = new MockMultipartFile(
         "file", "logo.png", MediaType.IMAGE_PNG_VALUE, imageBytes
      );
 
      // upload response data
      OrganizationImageResponseDTO organizationImageResponseDTO = new OrganizationImageResponseDTO(
         "organizationImage123", organizationId, "logo.png", 
         MediaType.IMAGE_PNG_VALUE, (long) imageBytes.length, LocalDateTime.now()
      );
 
      // stubs
      when(this.organizationImageService.uploadOrganizationImage(eq(organizationId), eq(userId), any()))
         .thenReturn(organizationImageResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         multipart(this.BASE_PATH + "/" + organizationId + "/user/" + userId)
         .file(file)
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(organizationImageResponseDTO.id()))
         .andExpect(jsonPath("$.data.organizationId").value(organizationId.toString()))
         .andExpect(jsonPath("$.data.fileName").value(organizationImageResponseDTO.fileName()))
         .andExpect(jsonPath("$.data.mimeType").value(organizationImageResponseDTO.mimeType()))
         .andExpect(jsonPath("$.data.size").value(organizationImageResponseDTO.size()))
         .andExpect(jsonPath("$.data.uploadDate").isNotEmpty());
 
      // checks
      verify(this.organizationImageService).uploadOrganizationImage(eq(organizationId), eq(userId), any());
   }
 
 
   @Test
   @DisplayName("GET - /organization/logo-image/{organizationId} - should return the image data and status 200")
   void getOrganizationImage() throws Exception {
      // organization image data
      UUID organizationId = UUID.randomUUID();
      OrganizationImageDocument document = new OrganizationImageDocument.Builder()
         .organizationId(organizationId)
         .fileName("logo.png")
         .mimeType(MediaType.IMAGE_PNG_VALUE)
         .size(18L)
         .uploadDate(LocalDateTime.now())
         .binary("fake-image-content".getBytes())
         .build();
 
      // image response data
      OrganizationImageResponseDTO organizationImageResponseDTO = new OrganizationImageResponseDTO(
         "organizationImage123", organizationId, "logo.png", 
         MediaType.IMAGE_PNG_VALUE, 18L, LocalDateTime.now()
      );
 
      // stubs
      when(this.organizationImageService.getOrganizationImage(organizationId)).thenReturn(document);
      when(this.organizationImageMapper.toOrganizationImageResponseDTO(document)).thenReturn(organizationImageResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + organizationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(organizationImageResponseDTO.id()))
         .andExpect(jsonPath("$.data.organizationId").value(organizationId.toString()))
         .andExpect(jsonPath("$.data.fileName").value(organizationImageResponseDTO.fileName()))
         .andExpect(jsonPath("$.data.mimeType").value(organizationImageResponseDTO.mimeType()))
         .andExpect(jsonPath("$.data.size").value(organizationImageResponseDTO.size()));
 
      // checks
      verify(this.organizationImageService).getOrganizationImage(organizationId);
      verify(this.organizationImageMapper).toOrganizationImageResponseDTO(document);
   }
 
 
   @Test
   @DisplayName("GET - /organization/logo-image/{organizationId}/download - should return the image bytes and status 200")
   void getOrganizationImageData() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      byte[] imageBytes = "fake-image-content".getBytes();
      OrganizationImageDocument document = new OrganizationImageDocument.Builder()
         .organizationId(organizationId)
         .fileName("logo.png")
         .mimeType(MediaType.IMAGE_PNG_VALUE)
         .size((long) imageBytes.length)
         .uploadDate(LocalDateTime.now())
         .binary(imageBytes)
         .build();
 
      // stubs
      when(this.organizationImageService.getOrganizationImage(organizationId)).thenReturn(document);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + organizationId + "/download")
         .accept(MediaType.IMAGE_PNG)
      )
         .andExpect(status().isOk())
         .andExpect(content().contentType(MediaType.IMAGE_PNG))
         .andExpect(header().string("Content-Disposition", "inline; filename=\"logo.png\""))
         .andExpect(content().bytes(imageBytes));
 
      // checks
      verify(this.organizationImageService).getOrganizationImage(organizationId);
   }
 
 
   @Test
   @DisplayName("DELETE - /organization/logo-image/{organizationId}/user/{userId} - should return status 200")
   void deleteOrganizationImage() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/" + organizationId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // checks
      verify(this.organizationImageService).deleteOrganizationImage(organizationId, userId);
   }

}