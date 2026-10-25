
// packages
package com.example.ProjectFlow.modules.labels.unitTests;

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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.labels.repository.LabelsRepository;
 
// import validator
import com.example.ProjectFlow.modules.labels.validator.LabelsValidator;
 
// import DTOs
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsDeletedDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsResponseDTO;
import com.example.ProjectFlow.modules.labels.dto.labelsDTO.LabelsUpdateDTO;
 
// import service
import com.example.ProjectFlow.modules.project.service.ProjectService;
import com.example.ProjectFlow.modules.labels.service.LabelService;
 
// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;
import com.example.ProjectFlow.modules.labels.entity.LabelsEntity;
 
// import mapper
import com.example.ProjectFlow.modules.labels.mapper.LabelsMapper;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - LabelService")
public class LabelHappyTests {
 
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
   @DisplayName("create() - must create a label")
   void create() {
      // id's - setup
      UUID labelId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // project - setup
      ProjectEntity projectEntity = new ProjectEntity.Builder()
         .id(projectId)
         .build();
 
      // label - setup
      LabelsEntity labelEntity = new LabelsEntity.Builder()
         .id(labelId)
         .name("label x")
         .color("#ff0000")
         .build();
 
      // DTO's - setup
      LabelsDTO labelsDTO = new LabelsDTO(
         projectId, labelEntity.getName(), labelEntity.getColor()
      );
 
      LabelsResponseDTO labelsResponseDTO = new LabelsResponseDTO(
         labelId, projectId, labelEntity.getName(), labelEntity.getColor()
      );
 
      // stubs
      when(this.projectService.getEntityById(projectId)).thenReturn(projectEntity);
      when(this.labelsRepository.create(labelsDTO, projectEntity)).thenReturn(labelEntity);
      when(this.labelsMapper.toLabelsResponseDTO(labelEntity)).thenReturn(labelsResponseDTO);
 
      // act and assert
      LabelsResponseDTO result = this.labelService.create(labelsDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(labelsResponseDTO);
 
      // checks
      verify(this.labelsValidator, times(1)).projectIdValidate(projectId);
      verify(this.labelsValidator, times(1)).nameValidate(labelsDTO.name());
      verify(this.labelsValidator, times(1)).colorValidate(labelsDTO.color());

      verify(this.projectService, times(1)).getEntityById(projectId);
      verify(this.labelsRepository, times(1)).create(labelsDTO, projectEntity);
      
      verify(this.labelsMapper, times(1)).toLabelsResponseDTO(labelEntity);
   }


   @Test
   @DisplayName("getAllByProjectId() - must return all labels by project id")
   void getAllByProjectId() {
      // id's - setup
      UUID labelId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // label - setup
      LabelsEntity labelEntity = new LabelsEntity.Builder()
         .id(labelId)
         .name("label x")
         .color("#ff0000")
         .build();
 
      // list - setup
      List<LabelsEntity> labelList = List.of(labelEntity);
 
      // DTO - setup
      LabelsResponseDTO labelsResponseDTO = new LabelsResponseDTO(
         labelId, projectId, labelEntity.getName(), labelEntity.getColor()
      );
 
      // stubs
      when(this.labelsRepository.getAllByProjectId(projectId)).thenReturn(labelList);
      when(this.labelsMapper.toLabelsResponseDTO(labelEntity)).thenReturn(labelsResponseDTO);
 
      // act and assert
      List<LabelsResponseDTO> result = this.labelService.getAllByProjectId(projectId);
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(labelsResponseDTO);
 
      // checks
      verify(this.labelsValidator, times(1)).projectIdValidate(projectId);
      verify(this.labelsRepository, times(1)).getAllByProjectId(projectId);
      verify(this.labelsMapper, times(1)).toLabelsResponseDTO(labelEntity);
   }
 
 
   @Test
   @DisplayName("getById() - must return label by id")
   void getById() {
      // id's - setup
      UUID labelId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // label - setup
      LabelsEntity labelEntity = new LabelsEntity.Builder()
         .id(labelId)
         .name("label x")
         .color("#ff0000")
         .build();
 
      // DTO - setup
      LabelsResponseDTO labelsResponseDTO = new LabelsResponseDTO(
         labelId, projectId, labelEntity.getName(), labelEntity.getColor()
      );
 
      // stubs
      when(this.labelsRepository.getById(labelId)).thenReturn(labelEntity);
      when(this.labelsMapper.toLabelsResponseDTO(labelEntity)).thenReturn(labelsResponseDTO);
 
      // act and assert
      LabelsResponseDTO result = this.labelService.getById(labelId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(labelsResponseDTO);
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsRepository, times(1)).getById(labelId);
      verify(this.labelsMapper, times(1)).toLabelsResponseDTO(labelEntity);
   }
 
 
   @Test
   @DisplayName("getEntityById() - must return label entity by id")
   void getEntityById() {
      // id's - setup
      UUID labelId = UUID.randomUUID();
 
      // label - setup
      LabelsEntity labelEntity = new LabelsEntity.Builder()
         .id(labelId)
         .build();
 
      // stubs
      when(this.labelsRepository.getEntityById(labelId)).thenReturn(labelEntity);
 
      // act and assert
      LabelsEntity result = this.labelService.getEntityById(labelId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(labelEntity);
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsRepository, times(1)).getEntityById(labelId);
   }
 
 
   @Test
   @DisplayName("existsById() - must check if label exist by id")
   void existsById() {
      // id's - setup
      UUID labelId = UUID.randomUUID();
 
      // stubs
      when(this.labelsRepository.existsById(labelId)).thenReturn(true);
 
      // act and assert
      boolean result = this.labelService.existsById(labelId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsRepository, times(1)).existsById(labelId);
   }


   @Test
   @DisplayName("update() - must update label (only the name)")
   void update() {
      // id's - setup
      UUID labelId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // data to update
      String newName = "label atualizada";
 
      // label - setup (already updated)
      LabelsEntity labelEntity = new LabelsEntity.Builder()
         .id(labelId)
         .name(newName)
         .color("#ff0000")
         .build();
 
      // DTO's - setup
      LabelsUpdateDTO labelsUpdateDTO = new LabelsUpdateDTO(
         newName, labelEntity.getColor()
      );
 
      LabelsResponseDTO labelsResponseDTO = new LabelsResponseDTO(
         labelId, projectId, labelEntity.getName(), labelEntity.getColor()
      );
 
      // stubs
      when(this.labelsRepository.update(labelId, labelsUpdateDTO)).thenReturn(labelEntity);
      when(this.labelsMapper.toLabelsResponseDTO(labelEntity)).thenReturn(labelsResponseDTO);
 
      // act and assert
      LabelsResponseDTO result = this.labelService.update(labelId, labelsUpdateDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(labelsResponseDTO);
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsValidator, times(1)).updateValidation(labelsUpdateDTO);
      
      verify(this.labelsRepository, times(1)).update(labelId, labelsUpdateDTO);
      verify(this.labelsMapper, times(1)).toLabelsResponseDTO(labelEntity);
   }
 
 
   @Test
   @DisplayName("delete() - must delete a label")
   void delete() {
      // id's - setup
      UUID labelId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
 
      // label - setup
      LabelsEntity labelEntity = new LabelsEntity.Builder()
         .id(labelId)
         .name("label x")
         .color("#ff0000")
         .build();
 
      // DTO - setup
      LabelsDeletedDTO labelsDeletedDTO = new LabelsDeletedDTO(
         labelId, projectId, labelEntity.getName(), labelEntity.getColor(),
         LocalDateTime.now()
      );
 
      // stubs
      when(this.labelsRepository.delete(labelId)).thenReturn(labelEntity);
      when(this.labelsMapper.toLabelsDeletedDTO(labelEntity)).thenReturn(labelsDeletedDTO);
 
      // act and assert
      LabelsDeletedDTO result = this.labelService.delete(labelId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(labelsDeletedDTO);
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsRepository, times(1)).delete(labelId);
      verify(this.labelsMapper, times(1)).toLabelsDeletedDTO(labelEntity);
   }
 
 
   @Test
   @DisplayName("isDeleted() - must check if label is deleted")
   void isDeleted() {
      // id's - setup
      UUID labelId = UUID.randomUUID();
 
      // stubs
      when(this.labelsRepository.isDeleted(labelId)).thenReturn(true);
 
      // act and assert
      boolean result = this.labelService.isDeleted(labelId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.labelsValidator, times(1)).idValidate(labelId);
      verify(this.labelsRepository, times(1)).isDeleted(labelId);
   }

}