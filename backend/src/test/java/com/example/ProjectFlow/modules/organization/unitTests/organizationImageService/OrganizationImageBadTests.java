
// packages
package com.example.ProjectFlow.modules.organization.unitTests.organizationImageService;

// imports
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// import repository
import com.example.ProjectFlow.modules.organization.repository.OrganizationImageRepository;

// import validator
import com.example.ProjectFlow.modules.organization.validator.OrganizationImageValidator;

// import services
import com.example.ProjectFlow.modules.organization.service.OrganizationService;
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
import com.example.ProjectFlow.modules.organization.service.OrganizationImageService;
import com.example.ProjectFlow.modules.organization.document.OrganizationImageDocument;

// import mapper
import com.example.ProjectFlow.modules.organization.mapper.OrganizationImageMapper;


@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - OrganizationImageService")
public class OrganizationImageBadTests {
 
   // mock dependencies
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
   @DisplayName("uploadOrganizationImage() - must throw when file cannot be read")
   void uploadOrganizationImage() throws IOException {
      // id's - setup
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();

      // file - mock
      MultipartFile file = mock(MultipartFile.class);

      // stubs
      when(this.organizationImageRepository.existsByOrganizationId(organizationId)).thenReturn(false);
      when(file.getBytes()).thenThrow(new IOException("Falha ao processar imagem da organização"));

      // act and assert
      assertThatThrownBy(() -> this.organizationImageService.uploadOrganizationImage(organizationId, userId, file))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Erro interno do servidor: Falha ao processar imagem da organização");

      // checks
      verify(this.organizationImageValidator, times(1)).validate(file);
      verify(this.organizationService, times(1)).userIsAdmin(userId, organizationId);
      verify(this.organizationImageRepository, times(1)).existsByOrganizationId(organizationId);

      // no checks
      verify(this.organizationImageRepository, never()).save(any());
      verify(this.organizationService, never()).updateLogoImageId(any(UUID.class), anyString());
      verify(this.organizationService, never()).getEntityById(any(UUID.class));
      verifyNoInteractions(this.activityLogService);
      verifyNoInteractions(this.organizationImageMapper);
   }


   @Test
   @DisplayName("getOrganizationImage() - must throw when organization image does not exist")
   void getOrganizationImage() {
      // id - setup
      UUID organizationId = UUID.randomUUID();

      // stubs
      when(this.organizationImageRepository.findByOrganizationId(organizationId)).thenReturn(null);

      // act and assert
      assertThatThrownBy(() -> this.organizationImageService.getOrganizationImage(organizationId))
         .isInstanceOf(RuntimeException.class)
         .hasMessage("Recurso não encontrado: Imagem de organização não existe");

      // checks
      verify(this.organizationService, times(1)).existsById(organizationId);
      verify(this.organizationImageRepository, times(1)).findByOrganizationId(organizationId);      
   }


   @Test
   @DisplayName("getAllOrganizationImages() - must throw when organization images list is empty")
   void getAllOrganizationImages() {
      // list - setup
      List<OrganizationImageDocument> documentList = List.of();

      // stubs
      when(this.organizationImageRepository.findAll()).thenReturn(documentList);

      // act and assert
      assertThatThrownBy(() -> this.organizationImageService.getAllOrganizationImages())
         .isInstanceOf(RuntimeException.class)
         .hasMessage("Recurso não encontrado: Imagens de organizações não existem");

      // checks
      verify(this.organizationImageRepository, times(1)).findAll();    
   }


   @Test
   @DisplayName("deleteOrganizationImage() - must throw an error when organization image does not exist")
   void deleteOrganizationImage() {
      // id's - setup
      UUID userId = UUID.randomUUID();
      UUID organizationId = UUID.randomUUID();

      // stubs
      when(this.organizationImageRepository.findByOrganizationId(organizationId)).thenReturn(null);

      // act and assert
      assertThatThrownBy(() -> this.organizationImageService.deleteOrganizationImage(organizationId, userId))
         .isInstanceOf(RuntimeException.class)
         .hasMessage("Recurso não encontrado: Imagem de organização não existe");

      // checks
      verify(this.organizationService, times(1)).userIsAdmin(userId, organizationId);
      verify(this.organizationImageRepository, times(1)).findByOrganizationId(organizationId);

      // no checks
      verify(this.organizationService, never()).removeLogoImageId(any(UUID.class));
      verify(this.organizationImageRepository, never()).deleteByOrganizationId(any(UUID.class));
      verify(this.organizationService, never()).getEntityById(any(UUID.class));
      verifyNoInteractions(this.activityLogService);
   }

}