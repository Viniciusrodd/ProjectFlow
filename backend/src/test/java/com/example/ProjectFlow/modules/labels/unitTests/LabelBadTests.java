
// packages
package com.example.ProjectFlow.modules.labels.unitTests;

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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.labels.repository.LabelsRepository;
 
// import validator
import com.example.ProjectFlow.modules.labels.validator.LabelsValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsUpdateDTO;
 
// import service
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.labels.service.LabelService;
 
// import entity
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;
 
// import mapper
import com.example.ProjectFlow.modules.labels.mapper.LabelsMapper;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;
 
// import constants
import com.example.ProjectFlow.common.constants.ResponseMessages;
 
// jakarta imports
import jakarta.persistence.NoResultException;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - LabelService")
public class LabelBadTests {
 
   // mock dependencies
   @Mock
   private LabelsRepository labelsRepository;
   @Mock
   private LabelsValidator labelsValidator;
   @Mock
   private ProjectService projectService;
   @Mock
   private LabelsMapper labelsMapper;
 
   // real instance for inject all mock objects above
   @InjectMocks
   private LabelService labelService;
 
 
   @Test
   @DisplayName("create() - must throw when project does not exist")
   void create() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
 
      // error message - setup
      String errorMessage = "Recurso não encontrado: Projeto não existe";

      // DTO - setup
      LabelsDTO labelsDTO = new LabelsDTO(
         projectId, "label x", "#ff0000"
      );
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage))
         .when(this.projectService).getEntityById(projectId);
 
      // act and assert
      assertThatThrownBy(() -> this.labelService.create(labelsDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining(errorMessage);
 
      // checks
      verify(this.labelsValidator, times(1)).projectIdValidate(projectId);
      verify(this.labelsValidator, times(1)).nameValidate(labelsDTO.name());
      verify(this.labelsValidator, times(1)).colorValidate(labelsDTO.color());
      verify(this.projectService, times(1)).getEntityById(projectId);
 
      // no checks
      verifyNoInteractions(this.labelsRepository);
      verifyNoInteractions(this.labelsMapper);
   }
 
 
   @Test
   @DisplayName("getAllByProjectId() - must throw when label list is empty")
   void getAllByProjectId() {
      // id - setup
      UUID projectId = UUID.randomUUID();
 
      // list - setup
      List<LabelsEntity> labelList = List.of();
 
      // stubs
      when(this.labelsRepository.getAllByProjectId(projectId)).thenReturn(labelList);
 
      // act and assert
      assertThatThrownBy(() -> this.labelService.getAllByProjectId(projectId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiquetas do projeto não existem");
 
      // checks
      verify(this.labelsValidator, times(1)).projectIdValidate(projectId);
      verify(this.labelsRepository, times(1)).getAllByProjectId(projectId);
 
      // no checks
      verifyNoInteractions(this.labelsMapper);
   }
 
 
   @Test
   @DisplayName("getById() - must throw label not found")
   void getById() {
      // id - setup
      UUID labelId = UUID.randomUUID();
 
      // stubs
      when(this.labelsRepository.getById(labelId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.labelService.getById(labelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiqueta de projeto não existe");
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsRepository, times(1)).getById(labelId);
 
      // no checks
      verifyNoInteractions(this.labelsMapper);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must throw label not found")
   void getEntityById() {
      // id - setup
      UUID labelId = UUID.randomUUID();
 
      // stubs
      when(this.labelsRepository.getEntityById(labelId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.labelService.getEntityById(labelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiqueta de projeto não existe");
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsRepository, times(1)).getEntityById(labelId);
   }
 
 
   @Test
   @DisplayName("existsById() - must throw when label does not exist")
   void existsById() {
      // id - setup
      UUID labelId = UUID.randomUUID();
 
      // stubs
      when(this.labelsRepository.existsById(labelId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.labelService.existsById(labelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiqueta de projeto não existe");
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsRepository, times(1)).existsById(labelId);
   }
 
 
   @Test
   @DisplayName("update() - must throw when label does not exist")
   void update() {
      // id - setup
      UUID labelId = UUID.randomUUID();
 
      // DTO - setup
      LabelsUpdateDTO labelsUpdateDTO = new LabelsUpdateDTO(
         "label atualizada", "#ff0000"
      );
 
      // stubs
      when(this.labelsRepository.update(labelId, labelsUpdateDTO)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.labelService.update(labelId, labelsUpdateDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiqueta de projeto não existe");
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsValidator, times(1)).updateValidation(labelsUpdateDTO);
      verify(this.labelsRepository, times(1)).update(labelId, labelsUpdateDTO);
 
      // no checks
      verifyNoInteractions(this.labelsMapper);
   }
 
 
   @Test
   @DisplayName("delete() - must throw when label does not exist")
   void delete() {
      // id - setup
      UUID labelId = UUID.randomUUID();
 
      // stubs
      when(this.labelsRepository.delete(labelId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.labelService.delete(labelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiqueta de projeto não existe");
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsRepository, times(1)).delete(labelId);
 
      // no checks
      verifyNoInteractions(this.labelsMapper);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must throw when label does not exist")
   void isDeleted() {
      // id - setup
      UUID labelId = UUID.randomUUID();
 
      // stubs
      when(this.labelsRepository.isDeleted(labelId)).thenThrow(NoResultException.class);
 
      // act and assert
      assertThatThrownBy(() -> this.labelService.isDeleted(labelId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Etiqueta de projeto não existe");
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsRepository, times(1)).isDeleted(labelId);
   }

}