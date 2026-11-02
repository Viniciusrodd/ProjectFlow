
// packages
package com.example.ProjectFlow.modules.project.integrationTests.projectController;

// imports
import java.util.UUID;
 
// jackson imports
import tools.jackson.databind.ObjectMapper;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 
// mockito imports
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.oauth2.jwt.JwtDecoder;
 
// spring imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 
// import controller
import com.example.ProjectFlow.modules.project.controller.ProjectController;
 
// import services
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.project.service.ProjectCreationService;
 
// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectDTO;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectUpdateDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
 
@WebMvcTest(ProjectController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - ProjectController")
public class ProjectBadTests {

   // for reqs http simulation
   @Autowired
   private MockMvc mockMvc;
   
   // to convert java objects <-> json
   @Autowired
   private ObjectMapper objectMapper;
   
   // mock's for disable security filter in test
   @MockitoBean
   private JwtDecoder jwtDecoder;
   @MockitoBean
   private JwtFilter jwtFilter;
 
   // replaces real services for spring context mock
   @MockitoBean 
   private ProjectService projectService;
   @MockitoBean 
   private ProjectCreationService projectCreationService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;


   @Test
   @DisplayName("POST - /project - should return 404 when owner does not exist")
   void createProject() throws Exception {
      // data - setup
      ProjectDTO projectDTO = new ProjectDTO(
         UUID.randomUUID(), UUID.randomUUID(), "ProjectFlow API", "Backend of the ProjectFlow"
      );
 
      // error message
      String errorMessage = "Recurso não encontrado: Usuário não existe";
 
      // stubs
      when(this.projectCreationService.create(projectDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/project")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(projectDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectCreationService).create(projectDTO);
   }
 
 
   @Test
   @DisplayName("GET - /projects - should return 404 when there are no projects")
   void getAllProjects() throws Exception {
      // error message
      String errorMessage = "Recurso não encontrado: Projetos não existem";
 
      // stubs
      when(this.projectService.getAll()).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/projects")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /project/{id} - should return 404 when project does not exist")
   void getProjectById() throws Exception {
      // project id
      UUID projectId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Projeto não existe";
 
      // stubs
      when(this.projectService.getById(projectId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/project/" + projectId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectService).getById(projectId);
   }
 
 
   @Test
   @DisplayName("GET - /projects/organization/{organizationId} - should return 404 when organization has no projects")
   void getByOrganizationId() throws Exception {
      // organization id
      UUID organizationId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Projetos não existem";
 
      // stubs
      when(this.projectService.getByOrganizationId(organizationId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/projects/organization/" + organizationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectService).getByOrganizationId(organizationId);
   }
 
 
   @Test
   @DisplayName("GET - /projects/owner/{ownerId} - should return 404 when owner has no projects")
   void getByOwnerId() throws Exception {
      // owner id
      UUID ownerId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Projetos não existem";
 
      // stubs
      when(this.projectService.getByOwnerId(ownerId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/projects/owner/" + ownerId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectService).getByOwnerId(ownerId);
   }
 
 
   @Test
   @DisplayName("PUT - /project/{id}/user/{userId} - should return 404 when project does not exist")
   void updateProject() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      ProjectUpdateDTO updateDTO = new ProjectUpdateDTO("ProjectFlow API updated", "New description", "ACTIVE");
 
      // error message
      String errorMessage = "Recurso não encontrado: Projeto não existe";
 
      // stubs
      when(this.projectService.update(projectId, userId, updateDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/project/" + projectId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectService).update(projectId, userId, updateDTO);
   }
 
 
   @Test
   @DisplayName("DELETE - /project/{id}/user/{userId} - should return 404 when project does not exist")
   void deleteProjectNotFound() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Projeto não existe";
 
      // stubs
      when(this.projectService.delete(projectId, userId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/project/" + projectId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.projectService).delete(projectId, userId);
   }

}