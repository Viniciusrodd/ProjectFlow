
// packages
package com.example.ProjectFlow.modules.project.integrationTests.projectController;

// imports
import java.time.LocalDateTime;
import java.util.List;
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
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectDeletedDTO;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectResponseDTO;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectUpdateDTO;
 
// import enums
import com.example.ProjectFlow.modules.project.enums.StatusEnum;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
 
@WebMvcTest(ProjectController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
public class ProjectHappyTests {

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
   @DisplayName("POST - /project - should return created project and status 201")
   void createProject() throws Exception {
      // data - setup
      UUID organizationId = UUID.randomUUID();
      UUID ownerId = UUID.randomUUID();
      ProjectDTO projectDTO = new ProjectDTO(
         organizationId, ownerId, "ProjectFlow API", "Backend of the ProjectFlow"
      );
 
      // project response data
      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         UUID.randomUUID(), organizationId, ownerId, 
         projectDTO.name(), projectDTO.description(), StatusEnum.ACTIVE
      );
 
      // stubs
      when(this.projectCreationService.create(projectDTO)).thenReturn(projectResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/project")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(projectDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(projectResponseDTO.id().toString()))
         .andExpect(jsonPath("$.data.organizationId").value(organizationId.toString()))
         .andExpect(jsonPath("$.data.ownerId").value(ownerId.toString()))
         .andExpect(jsonPath("$.data.name").value(projectResponseDTO.name()))
         .andExpect(jsonPath("$.data.description").value(projectResponseDTO.description()))
         .andExpect(jsonPath("$.data.status").value(projectResponseDTO.status().name()));
 
      // checks
      verify(this.projectCreationService).create(projectDTO);
   }
 
 
   @Test
   @DisplayName("GET - /projects - should return all projects and status 200")
   void getAllProjects() throws Exception {
      // projects data
      List<ProjectResponseDTO> projects = List.of(
         new ProjectResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 
            "ProjectFlow API", "First project", StatusEnum.ACTIVE
         ),
         new ProjectResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 
            "ProjectFlow Desktop", "Second project", StatusEnum.ACTIVE
         )
      );
 
      // stubs
      when(this.projectService.getAll()).thenReturn(projects);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/projects")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(projects.size()))
         .andExpect(jsonPath("$.data[0].id").value(projects.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].organizationId").value(projects.get(0).organizationId().toString()))
         .andExpect(jsonPath("$.data[0].ownerId").value(projects.get(0).ownerId().toString()))
         .andExpect(jsonPath("$.data[0].name").value(projects.get(0).name()))
         .andExpect(jsonPath("$.data[0].description").value(projects.get(0).description()))
         .andExpect(jsonPath("$.data[0].status").value(projects.get(0).status().name()))
         .andExpect(jsonPath("$.data[1].id").value(projects.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].organizationId").value(projects.get(1).organizationId().toString()))
         .andExpect(jsonPath("$.data[1].ownerId").value(projects.get(1).ownerId().toString()))
         .andExpect(jsonPath("$.data[1].name").value(projects.get(1).name()))
         .andExpect(jsonPath("$.data[1].description").value(projects.get(1).description()))
         .andExpect(jsonPath("$.data[1].status").value(projects.get(1).status().name()));
 
      // checks
      verify(this.projectService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /project/{id} - should return the project and status 200")
   void getProjectById() throws Exception {
      // project data
      UUID projectId = UUID.randomUUID();
      ProjectResponseDTO project = new ProjectResponseDTO(
         projectId, UUID.randomUUID(), UUID.randomUUID(), 
         "ProjectFlow API", "Backend of the ProjectFlow", StatusEnum.ACTIVE
      );
 
      // stubs
      when(this.projectService.getById(projectId)).thenReturn(project);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/project/" + projectId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(project.id().toString()))
         .andExpect(jsonPath("$.data.organizationId").value(project.organizationId().toString()))
         .andExpect(jsonPath("$.data.ownerId").value(project.ownerId().toString()))
         .andExpect(jsonPath("$.data.name").value(project.name()))
         .andExpect(jsonPath("$.data.description").value(project.description()))
         .andExpect(jsonPath("$.data.status").value(project.status().name()));
 
      // checks
      verify(this.projectService).getById(projectId);
   }
 
 
   @Test
   @DisplayName("GET - /projects/organization/{organizationId} - should return the organization projects and status 200")
   void getByOrganizationId() throws Exception {
      // projects data
      UUID organizationId = UUID.randomUUID();
      List<ProjectResponseDTO> projects = List.of(
         new ProjectResponseDTO(
            UUID.randomUUID(), organizationId, UUID.randomUUID(), 
            "ProjectFlow API", "First project", StatusEnum.ACTIVE
         ),
         new ProjectResponseDTO(
            UUID.randomUUID(), organizationId, UUID.randomUUID(), 
            "ProjectFlow Desktop", "Second project", StatusEnum.ACTIVE
         )
      );
 
      // stubs
      when(this.projectService.getByOrganizationId(organizationId)).thenReturn(projects);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/projects/organization/" + organizationId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(projects.size()))
         .andExpect(jsonPath("$.data[0].id").value(projects.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].organizationId").value(organizationId.toString()))
         .andExpect(jsonPath("$.data[0].name").value(projects.get(0).name()))
         .andExpect(jsonPath("$.data[1].id").value(projects.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].organizationId").value(organizationId.toString()))
         .andExpect(jsonPath("$.data[1].name").value(projects.get(1).name()));
 
      // checks
      verify(this.projectService).getByOrganizationId(organizationId);
   }
 
 
   @Test
   @DisplayName("GET - /projects/owner/{ownerId} - should return the owner projects and status 200")
   void getByOwnerId() throws Exception {
      // projects data
      UUID ownerId = UUID.randomUUID();
      List<ProjectResponseDTO> projects = List.of(
         new ProjectResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), ownerId, 
            "ProjectFlow API", "First project", StatusEnum.ACTIVE
         ),
         new ProjectResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), ownerId, 
            "ProjectFlow Desktop", "Second project", StatusEnum.ACTIVE
         )
      );
 
      // stubs
      when(this.projectService.getByOwnerId(ownerId)).thenReturn(projects);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/projects/owner/" + ownerId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(projects.size()))
         .andExpect(jsonPath("$.data[0].id").value(projects.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].ownerId").value(ownerId.toString()))
         .andExpect(jsonPath("$.data[0].name").value(projects.get(0).name()))
         .andExpect(jsonPath("$.data[1].id").value(projects.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].ownerId").value(ownerId.toString()))
         .andExpect(jsonPath("$.data[1].name").value(projects.get(1).name()));
 
      // checks
      verify(this.projectService).getByOwnerId(ownerId);
   }
 
 
   @Test
   @DisplayName("PUT - /project/{id}/user/{userId} - should return status 200 when updating (only the name)")
   void updateProject() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      ProjectUpdateDTO updateDTO = new ProjectUpdateDTO("ProjectFlow API updated", null, null);
 
      // update response data
      ProjectResponseDTO updatedProject = new ProjectResponseDTO(
         projectId, UUID.randomUUID(), userId, 
         updateDTO.name(), "Backend of the ProjectFlow", StatusEnum.ACTIVE
      );
 
      // stubs
      when(this.projectService.update(projectId, userId, updateDTO)).thenReturn(updatedProject);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/project/" + projectId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(updateDTO))
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedProject.id().toString()))
         .andExpect(jsonPath("$.data.organizationId").value(updatedProject.organizationId().toString()))
         .andExpect(jsonPath("$.data.ownerId").value(updatedProject.ownerId().toString()))
         .andExpect(jsonPath("$.data.name").value(updatedProject.name()))
         .andExpect(jsonPath("$.data.description").value(updatedProject.description()))
         .andExpect(jsonPath("$.data.status").value(updatedProject.status().name()));
 
      // checks
      verify(this.projectService).update(projectId, userId, updateDTO);
   }
 
 
   @Test
   @DisplayName("DELETE - /project/{id}/user/{userId} - should return the deleted project and status 200")
   void deleteProject() throws Exception {
      // data - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      ProjectDeletedDTO deletedProject = new ProjectDeletedDTO(
         projectId, UUID.randomUUID(), userId, "ProjectFlow API", 
         "Backend of the ProjectFlow", StatusEnum.ACTIVE, LocalDateTime.now()
      );
 
      // stubs
      when(this.projectService.delete(projectId, userId)).thenReturn(deletedProject);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/project/" + projectId + "/user/" + userId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(deletedProject.id().toString()))
         .andExpect(jsonPath("$.data.organizationId").value(deletedProject.organizationId().toString()))
         .andExpect(jsonPath("$.data.ownerId").value(deletedProject.ownerId().toString()))
         .andExpect(jsonPath("$.data.name").value(deletedProject.name()))
         .andExpect(jsonPath("$.data.description").value(deletedProject.description()))
         .andExpect(jsonPath("$.data.status").value(deletedProject.status().name()))
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.projectService).delete(projectId, userId);
   }

}