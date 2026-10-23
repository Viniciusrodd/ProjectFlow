
// packages
package com.example.ProjectFlow.modules.project.unitTests.projectImageService;

// imports
import java.time.LocalDateTime;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import com.example.ProjectFlow.modules.project.dto.projectImageDTO.ProjectImageResponseDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;

// import mapper
import com.example.ProjectFlow.modules.project.mapper.ProjectImageMapper;

// import enums
import com.example.ProjectFlow.modules.project.enums.StatusEnum;
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - ProjectImageService")
public class ProjectImageHappyTests {

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
   @DisplayName("uploadProjectImage() - must upload an project image")
   void uploadProjectImage() {
      // id's - setup
      String projectImageId = UUID.randomUUID().toString();
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();
      
      // binary - setup
      byte[] binary = {1, 2, 3};

      // file - setup
      MultipartFile file = new MockMultipartFile(
         "file",
         "filename.png",
         "image/png",
         binary
      );

      // project image - setup
      ProjectImageDocument projectImageDocument = new ProjectImageDocument.Builder()
         .id(projectImageId)
         .projectId(projectId)
         .fileName("filename.png")
         .mimeType("image/png")
         .size(3L)
         .uploadDate(LocalDateTime.now())
         .binary(binary)
         .build();

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

      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .projectId(projectEntity.getId())
         .action(ActivityActionEnum.LOGO_DO_PROJETO_ATUALIZADO)
         .description("Imagem do projeto: " + projectEntity.getName() + ", criada")
         .build();
      
      ProjectImageResponseDTO projectImageResponseDTO = new ProjectImageResponseDTO(
         projectImageId, projectId, projectImageDocument.getFileName(), 
         projectImageDocument.getMimeType(), projectImageDocument.getSize(),
         LocalDateTime.now()
      );

      // stubs
      when(this.projectService.getById(projectId)).thenReturn(projectResponseDTO);
      when(this.projectImageRepository.existsByProjectId(projectId)).thenReturn(true);
      when(this.projectImageRepository.save(any(ProjectImageDocument.class))).thenReturn(projectImageDocument);      
      when(this.projectService.getEntityById(projectId)).thenReturn(projectEntity);      
      when(this.projectImageMapper.toProjectImageResponseDTO(projectImageDocument)).thenReturn(projectImageResponseDTO);      

      // act and assert
      ProjectImageResponseDTO result = this.projectImageService.uploadProjectImage(projectId, userId, file);
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectImageResponseDTO);

      // checks
      verify(this.projectImageValidator, times(1)).validate(file);
      verify(this.organizationService, times(1)).userIsAdmin(userId, projectResponseDTO.organizationId());
      verify(this.projectService, times(1)).getById(projectId);

      verify(this.projectImageRepository, times(1)).existsByProjectId(projectId);
      verify(this.projectImageRepository, times(1)).deleteByProjectId(projectId);

      verify(this.projectImageRepository, times(1)).save(any(ProjectImageDocument.class));
      verify(this.projectService, times(1)).updateLogoImageId(projectId, projectImageDocument.getId());
      verify(this.projectService, times(1)).getEntityById(projectId);
      
      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.projectImageMapper, times(1)).toProjectImageResponseDTO(projectImageDocument);
   }


   @Test
   @DisplayName("getProjectImage() - must get a project image")
   void getProjectImage() {
      // id - setup
      UUID projectId = UUID.randomUUID();

      // project image - setup
      ProjectImageDocument projectImageDocument = new ProjectImageDocument.Builder()
         .projectId(projectId)
         .build();

      // stubs
      when(this.projectImageRepository.findByProjectId(projectId)).thenReturn(projectImageDocument);

      // act and assert
      ProjectImageDocument result = this.projectImageService.getProjectImage(projectId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(projectImageDocument);

      // checks
      verify(this.projectService, times(1)).existsById(projectId);
      verify(this.projectImageRepository, times(1)).findByProjectId(projectId);
   }


   @Test
   @DisplayName("getAllProjectImages() - must return all project images")
   void getAllProjectImages() {
      // project image - setup
      ProjectImageDocument projectImageDocument = new ProjectImageDocument.Builder()
         .projectId(UUID.randomUUID())
         .build();

      // list - setup
      List<ProjectImageDocument> documentList = List.of(projectImageDocument);

      // stubs
      when(this.projectImageRepository.findAll()).thenReturn(documentList);

      // act and assert
      List<ProjectImageDocument> result = this.projectImageService.getAllProjectImages();
      assertThat(result)
         .isNotNull()
         .isEqualTo(documentList);

      // checks
      verify(this.projectImageRepository, times(1)).findAll();
   }


   @Test
   @DisplayName("deleteProjectImage() - must delete an project image")
   void deleteProjectImage() {
      // id's - setup
      String projectImageId = UUID.randomUUID().toString();
      UUID projectId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();

      // project image - setup
      ProjectImageDocument projectImageDocument = new ProjectImageDocument.Builder()
         .id(projectImageId)
         .build();

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

      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .projectId(projectEntity.getId())
         .action(ActivityActionEnum.LOGO_DO_PROJETO_REMOVIDO)
         .description("Imagem do projeto: " + projectEntity.getName() + ", removida")
         .build();

      // stubs
      when(this.projectService.getById(projectId)).thenReturn(projectResponseDTO);
      when(this.projectImageRepository.findByProjectId(projectId)).thenReturn(projectImageDocument);
      when(this.projectService.getEntityById(projectId)).thenReturn(projectEntity);      

      // act
      assertThatCode(() -> this.projectImageService.deleteProjectImage(projectId, userId))
         .doesNotThrowAnyException();

      // checks and assert (in this case)
      verify(this.organizationService, times(1)).userIsAdmin(userId, projectResponseDTO.organizationId());
      verify(this.projectService, times(1)).getById(projectId);

      verify(this.projectImageRepository, times(1)).findByProjectId(projectId);
      verify(this.projectService, times(1)).removeLogoImageId(projectId);
      verify(this.projectImageRepository, times(1)).deleteByProjectId(projectId);

      verify(this.projectService, times(1)).getEntityById(projectId);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
   }

}