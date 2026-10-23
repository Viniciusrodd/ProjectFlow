
// packages
package com.example.ProjectFlow.modules.project.unitTests.projectImageService;

// imports
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// import repository
import com.example.ProjectFlow.modules.project.repository.ProjectImageRepository;

// import services
import com.example.ProjectFlow.modules.project.service.ProjectImageService;
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.organization.service.OrganizationService;

// import validator
import com.example.ProjectFlow.modules.project.validator.ProjectImageValidator;

// import document
import com.example.ProjectFlow.modules.project.document.ProjectImageDocument;

// import DTO
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectResponseDTO;

// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;

// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectImageMapper;

// import enums
import com.example.ProjectFlow.modules.project.enums.StatusEnum;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - ProjectImageService")
public class ProjectImageBadTests {
 
   // mock dependencies
   @Mock
   private ProjectService projectService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private OrganizationService organizationService;
   @Mock
   private ProjectImageRepository projectImageRepository;
   @Mock
   private ProjectImageValidator projectImageValidator;
   @Mock
   private ProjectImageMapper projectImageMapper;

   // real instance for inject all mock objects above
   @InjectMocks  
   private ProjectImageService projectImageService;


   @Test
   @DisplayName("uploadProjectImage() - must throw an error when user is not a admin")
   void uploadProjectImage() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();
      
      // data - setup
      byte[] binary = {1, 2, 3};
      String errorMessage = "Não autorizado: Membro não é um administrador";

      // file - setup
      MultipartFile file = new MockMultipartFile(
         "file",
         "filename.png",
         "image/png",
         binary
      );
      
      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .build();

      // DTO - setup
      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectId, organizationId, userId, 
         projectEntity.getName(), projectEntity.getDescription(),
         StatusEnum.ACTIVE
      );

      // stubs
      when(this.projectService.getById(projectId)).thenReturn(projectResponseDTO);
      doThrow(MultiExceptions.unauthorized(errorMessage))
         .when(this.organizationService).userIsAdmin(userId, organizationId);

      // act and assert
      assertThatThrownBy(() -> this.projectImageService.uploadProjectImage(projectId, userId, file))
         .isInstanceOf(RuntimeException.class)
         .hasMessage(errorMessage);

      // checks
      verify(this.projectImageValidator, times(1)).validate(file);
      verify(this.projectService, times(1)).getById(projectId);
      verify(this.organizationService, times(1)).userIsAdmin(userId, projectResponseDTO.organizationId());
      
      // no checks
      verifyNoInteractions(this.projectImageRepository);
      verify(this.projectService, never()).updateLogoImageId(any(UUID.class), anyString());
      verify(this.projectService, never()).getEntityById(any(UUID.class));
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.projectImageMapper);
   }


   @Test
   @DisplayName("getProjectImage() - must throw when project image does not exist")
   void getProjectImage() {
      // id - setup
      UUID projectId = UUID.randomUUID();

      // stubs
      when(this.projectImageRepository.findByProjectId(projectId)).thenReturn(null);

      // act and assert
      assertThatThrownBy(() -> this.projectImageService.getProjectImage(projectId))
         .isInstanceOf(RuntimeException.class)
         .hasMessage("Recurso não encontrado: Imagem de projeto não existe");

      // checks
      verify(this.projectService, times(1)).existsById(projectId);
      verify(this.projectImageRepository, times(1)).findByProjectId(projectId);
   }


   @Test
   @DisplayName("getAllProjectImages() - must throw when project images list is empty")
   void getAllProjectImages() {
      // list - setup
      List<ProjectImageDocument> documentList = List.of();

      // stubs
      when(this.projectImageRepository.findAll()).thenReturn(documentList);

      // act and assert
      assertThatThrownBy(() -> this.projectImageService.getAllProjectImages())
         .isInstanceOf(RuntimeException.class)
         .hasMessage("Recurso não encontrado: Imagens de projetos não existem");

      // checks
      verify(this.projectImageRepository, times(1)).findAll();
   }


   @Test
   @DisplayName("deleteProjectImage() - must throw an error when project image does not exist")
   void deleteProjectImage() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();

      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .name("project x")
         .build();

      // DTO - setup
      ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO(
         projectId, organizationId, userId, 
         projectEntity.getName(), projectEntity.getDescription(),
         StatusEnum.ACTIVE
      );

      // stubs
      when(this.projectService.getById(projectId)).thenReturn(projectResponseDTO);
      when(this.projectImageRepository.findByProjectId(projectId)).thenReturn(null);

      // act and assert
      assertThatThrownBy(() -> this.projectImageService.deleteProjectImage(projectId, userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessage("Recurso não encontrado: Imagem de projeto não existe");

      // checks
      verify(this.organizationService, times(1)).userIsAdmin(userId, projectResponseDTO.organizationId());
      verify(this.projectService, times(1)).getById(projectId);
      verify(this.projectImageRepository, times(1)).findByProjectId(projectId);

      // no checks
      verify(this.projectService, never()).removeLogoImageId(any(UUID.class));
      verify(this.projectImageRepository, never()).deleteByProjectId(any(UUID.class));
      verify(this.projectService, never()).getEntityById(any(UUID.class));
      verifyNoInteractions(this.activityLogService);
   }

}