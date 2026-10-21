
// packages
package com.example.ProjectFlow.modules.organization.unitTests.organizationImageService;

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

// jakarta imports

// import repository
import com.example.ProjectFlow.modules.organization.repository.OrganizationImageRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationImageValidator;

// import DTOs
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationImageDTO.OrganizationImageResponseDTO;

// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.organization.service.OrganizationImageService;

// import document
import com.example.ProjectFlow.modules.organization.document.OrganizationImageDocument;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationEntity;

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationImageMapper;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - OrganizationImageService")
public class OrganizationImageHappyTests {
 
   @Mock
   private OrganizationImageRepository organizationImageRepository;
   @Mock
   private OrganizationService organizationService;
   @Mock
   private ActivityLogService activityLogService;
   @Mock
   private OrganizationImageValidator organizationImageValidator;
   @Mock
   private OrganizationImageMapper organizationImageMapper;
   
   // real instance for inject all mock objects above
   @InjectMocks  
   private OrganizationImageService organizationImageService;


   @Test
   @DisplayName("uploadOrganizationImage() - must upload an organization image")
   void uploadOrganizationImage() {
      // id - setup
      String organizationImageId = UUID.randomUUID().toString();
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

      // organization image - setup
      OrganizationImageDocument organizationImageDocument = new OrganizationImageDocument.Builder()
         .id(organizationImageId)
         .organizationId(organizationId)
         .fileName("filename.png")
         .mimeType("image/png")
         .size(3L)
         .uploadDate(LocalDateTime.now())
         .binary(binary)
         .build();

      // organization - setup
      OrganizationEntity organizationEntity = new OrganizationEntity.Builder()
         .id(organizationId)
         .name("organization x")
         .build();

      // DTO - setup
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .organizationId(organizationEntity.getId())
         .action(ActivityActionEnum.LOGO_DA_ORGANIZACAO_ATUALIZADO)
         .description("Imagem da organização: " + organizationEntity.getName() + ", criada")
         .build();

      OrganizationImageResponseDTO organizationImageResponseDTO = new OrganizationImageResponseDTO(
         organizationImageDocument.getId(), organizationImageDocument.getOrganizationId(),
         organizationImageDocument.getFileName(), organizationImageDocument.getMimeType(),
         organizationImageDocument.getSize(), LocalDateTime.now() 
      );

      // mocks config
      when(this.organizationImageRepository.existsByOrganizationId(organizationId)).thenReturn(true);
      when(this.organizationImageRepository.save(any(OrganizationImageDocument.class))).thenReturn(organizationImageDocument);
      when(this.organizationService.getEntityById(organizationId)).thenReturn(organizationEntity);
      when(this.organizationImageMapper.toOrganizationImageResponseDTO(organizationImageDocument)).thenReturn(organizationImageResponseDTO);

      // act and assert
      OrganizationImageResponseDTO result = this.organizationImageService.uploadOrganizationImage(organizationId, userId, file);
      assertThat(result)
         .isNotNull()
         .isEqualTo(organizationImageResponseDTO);

      // checks
      verify(this.organizationImageValidator, times(1)).validate(file);

      verify(this.organizationService, times(1)).userIsAdmin(userId, organizationId);
      verify(this.organizationImageRepository, times(1)).existsByOrganizationId(organizationId);
      verify(this.organizationImageRepository, times(1)).deleteByOrganizationId(organizationId);
      
      verify(this.organizationImageRepository, times(1)).save(any(OrganizationImageDocument.class));
      verify(this.organizationService, times(1)).updateLogoImageId(organizationId, organizationImageDocument.getId());
      verify(this.organizationService, times(1)).getEntityById(organizationId);

      verify(this.activityLogService, times(1)).create(activityLogDTO);
      verify(this.organizationImageMapper, times(1)).toOrganizationImageResponseDTO(organizationImageDocument);
   }


   @Test
   @DisplayName("getOrganizationImage() - must get a organization image")
   void getOrganizationImage() {
      // id - setup
      UUID organizationId = UUID.randomUUID();

      // organization image - setup
      OrganizationImageDocument organizationImageDocument = new OrganizationImageDocument.Builder()
         .organizationId(organizationId)
         .fileName("filename.png")
         .build();

      // mocks config
      when(this.organizationImageRepository.findByOrganizationId(organizationId)).thenReturn(organizationImageDocument);

      // act and assert
      OrganizationImageDocument result = this.organizationImageService.getOrganizationImage(organizationId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(organizationImageDocument);

      // checks
      verify(this.organizationService, times(1)).existsById(organizationId);
      verify(this.organizationImageRepository, times(1)).findByOrganizationId(organizationId);
   }


   @Test
   @DisplayName("getAllOrganizationImages() - must return all organization images")
   void getAllOrganizationImages() {
      // organization image - setup
      OrganizationImageDocument organizationImageDocument = new OrganizationImageDocument.Builder()
         .fileName("filename.png")
         .build();

      // list - setup
      List<OrganizationImageDocument> documentList = List.of(organizationImageDocument);

      // mocks config
      when(this.organizationImageRepository.findAll()).thenReturn(documentList);

      // act and assert
      List<OrganizationImageDocument> result = this.organizationImageService.getAllOrganizationImages();
      assertThat(result)
         .isNotNull()
         .isEqualTo(documentList);

      // checks
      verify(this.organizationImageRepository, times(1)).findAll();
   }


   @Test
   @DisplayName("deleteOrganizationImage() - must delete an organization image")
   void deleteOrganizationImage() {
      // id - setup
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();

      // organization image - setup
      OrganizationImageDocument organizationImageDocument = new OrganizationImageDocument.Builder()
         .fileName("filename.png")
         .build();

      // organization - setup
      OrganizationEntity organizationEntity = new OrganizationEntity.Builder()
         .id(organizationId)
         .name("organization x")
         .build();

      // DTO - setup
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .organizationId(organizationEntity.getId())
         .action(ActivityActionEnum.LOGO_DA_ORGANIZACAO_REMOVIDO)
         .description("Imagem da organização: " + organizationEntity.getName() + ", removida")
         .build();

      // mocks config
      when(this.organizationImageRepository.findByOrganizationId(organizationId)).thenReturn(organizationImageDocument);
      when(this.organizationService.getEntityById(organizationId)).thenReturn(organizationEntity);

      // act
      assertThatCode(() -> this.organizationImageService.deleteOrganizationImage(organizationId, userId))
         .doesNotThrowAnyException();

      // checks and assert (in this case)
      verify(this.organizationService, times(1)).userIsAdmin(userId, organizationId);
      verify(this.organizationImageRepository, times(1)).findByOrganizationId(organizationId);

      verify(this.organizationService, times(1)).removeLogoImageId(organizationId);
      verify(this.organizationImageRepository, times(1)).deleteByOrganizationId(organizationId);
      
      verify(this.organizationService, times(1)).getEntityById(organizationId);
      verify(this.activityLogService, times(1)).create(activityLogDTO);
   }
}