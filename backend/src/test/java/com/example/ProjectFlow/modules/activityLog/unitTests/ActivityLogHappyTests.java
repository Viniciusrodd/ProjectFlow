
// packages
package com.example.ProjectFlow.modules.activityLog.unitTests;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.activityLog.repository.ActivityLogRepository;
 
// import validator
import com.example.ProjectFlow.modules.activityLog.validator.ActivityLogValidator;
 
// import document
import com.example.ProjectFlow.modules.activityLog.document.ActivityLogDocument;
 
// import DTOs
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogResponseDTO;
 
// import service
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
 
// import mapper
import com.example.ProjectFlow.modules.activityLog.mapper.ActivityLogMapper;
 
// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Happy path) - ActivityLogService")
public class ActivityLogHappyTests {

   // mock dependencies
   @Mock
   private ActivityLogRepository activityLogRepository;
   @Mock
   private ActivityLogValidator activityLogValidator;
   @Mock
   private ActivityLogMapper activityLogMapper;
 
   // real instance for inject all mock objects above
   @InjectMocks
   private ActivityLogService activityLogService;


   @Test
   @DisplayName("create() - must create an activity log")
   void create() {
      // id's - setup
      String activityLogId = UUID.randomUUID().toString();
      UUID organizationId = UUID.randomUUID();
      UUID projectId = UUID.randomUUID();
      UUID taskId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
 
      // activity log - setup
      ActivityLogDocument activityLogDocument = new ActivityLogDocument.Builder()
         .id(activityLogId)
         .organizationId(organizationId)
         .projectId(projectId)
         .taskId(taskId)
         .userId(userId)
         .action(ActivityActionEnum.TAREFA_ATUALIZADA)
         .description("Tarefa: task x, atualizada")
         .createdAt(LocalDateTime.now())
         .build();
 
      // DTO's - setup
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .organizationId(organizationId)
         .projectId(projectId)
         .taskId(taskId)
         .userId(userId)
         .action(ActivityActionEnum.TAREFA_ATUALIZADA)
         .description("Tarefa: task x, atualizada")
         .build();
 
      ActivityLogResponseDTO activityLogResponseDTO = new ActivityLogResponseDTO(
         activityLogId, organizationId, projectId, taskId, userId,
         activityLogDocument.getAction(), activityLogDocument.getDescription()
      );
 
      // stubs
      when(this.activityLogRepository.save(any(ActivityLogDocument.class))).thenReturn(activityLogDocument);
      when(this.activityLogMapper.toActivityLogResponseDTO(activityLogDocument)).thenReturn(activityLogResponseDTO);
 
      // act and assert
      ActivityLogResponseDTO result = this.activityLogService.create(activityLogDTO);
      assertThat(result)
         .isNotNull()
         .isEqualTo(activityLogResponseDTO);
 
      // checks
      verify(this.activityLogValidator, times(1)).actionValidate(activityLogDTO.action().toString());
      verify(this.activityLogValidator, times(1)).descriptionValidate(activityLogDTO.description());
      verify(this.activityLogRepository, times(1)).save(any(ActivityLogDocument.class));
      verify(this.activityLogMapper, times(1)).toActivityLogResponseDTO(activityLogDocument);
   }


   @Test
   @DisplayName("getById() - must return activity log by id")
   void getById() {
      // id's - setup
      String activityLogId = UUID.randomUUID().toString();
      UUID projectId = UUID.randomUUID();
 
      // activity log - setup
      ActivityLogDocument activityLogDocument = new ActivityLogDocument.Builder()
         .id(activityLogId)
         .projectId(projectId)
         .action(ActivityActionEnum.TAREFA_ATUALIZADA)
         .description("Tarefa: task x, atualizada")
         .build();
 
      // DTO - setup
      ActivityLogResponseDTO activityLogResponseDTO = new ActivityLogResponseDTO(
         activityLogId, null, projectId, null, null,
         activityLogDocument.getAction(), activityLogDocument.getDescription()
      );
 
      // stubs
      when(this.activityLogRepository.findById(activityLogId)).thenReturn(Optional.of(activityLogDocument));
      when(this.activityLogMapper.toActivityLogResponseDTO(activityLogDocument)).thenReturn(activityLogResponseDTO);
 
      // act and assert
      ActivityLogResponseDTO result = this.activityLogService.getById(activityLogId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(activityLogResponseDTO);
 
      // checks
      verify(this.activityLogValidator, times(1)).idValidate(activityLogId);
      verify(this.activityLogRepository, times(1)).findById(activityLogId);
      verify(this.activityLogMapper, times(1)).toActivityLogResponseDTO(activityLogDocument);
   }
 
 
   @Test
   @DisplayName("getDocumentById() - must return activity log document by id")
   void getDocumentById() {
      // id's - setup
      String activityLogId = UUID.randomUUID().toString();
 
      // activity log - setup
      ActivityLogDocument activityLogDocument = new ActivityLogDocument.Builder()
         .id(activityLogId)
         .build();
 
      // stubs
      when(this.activityLogRepository.findById(activityLogId)).thenReturn(Optional.of(activityLogDocument));
 
      // act and assert
      ActivityLogDocument result = this.activityLogService.getDocumentById(activityLogId);
      assertThat(result)
         .isNotNull()
         .isEqualTo(activityLogDocument);
 
      // checks
      verify(this.activityLogValidator, times(1)).idValidate(activityLogId);
      verify(this.activityLogRepository, times(1)).findById(activityLogId);
   }
 
 
   @Test
   @DisplayName("existsById() - must check if activity log exist by id")
   void existsById() {
      // id's - setup
      String activityLogId = UUID.randomUUID().toString();
 
      // stubs
      when(this.activityLogRepository.existsById(activityLogId)).thenReturn(true);
 
      // act and assert
      boolean result = this.activityLogService.existsById(activityLogId);
      assertThat(result).isTrue();
 
      // checks
      verify(this.activityLogValidator, times(1)).idValidate(activityLogId);
      verify(this.activityLogRepository, times(1)).existsById(activityLogId);
   }
 
 
   @Test
   @DisplayName("getAllByDocumentId() - must return all activity logs (only by organization id)")
   void getAllByDocumentId() {
      // id's - setup
      String activityLogId = UUID.randomUUID().toString();
      UUID organizationId = UUID.randomUUID();
 
      // activity log - setup
      ActivityLogDocument activityLogDocument = new ActivityLogDocument.Builder()
         .id(activityLogId)
         .organizationId(organizationId)
         .action(ActivityActionEnum.TAREFA_ATUALIZADA)
         .build();
 
      // list - setup
      List<ActivityLogDocument> documentList = List.of(activityLogDocument);
 
      // DTO - setup
      ActivityLogResponseDTO activityLogResponseDTO = new ActivityLogResponseDTO(
         activityLogId, organizationId, null, null, null,
         activityLogDocument.getAction(), null
      );
 
      // stubs
      when(this.activityLogRepository.findByOrganizationId(organizationId)).thenReturn(documentList);
      when(this.activityLogMapper.toActivityLogResponseDTO(activityLogDocument)).thenReturn(activityLogResponseDTO);
 
      // act and assert
      List<ActivityLogResponseDTO> result = this.activityLogService.getAllByDocumentId(organizationId, "o");
      assertThat(result)
         .isNotNull()
         .hasSize(1)
         .contains(activityLogResponseDTO);
 
      // checks
      verify(this.activityLogRepository, times(1)).findByOrganizationId(organizationId);
      verify(this.activityLogMapper, times(1)).toActivityLogResponseDTO(activityLogDocument);
   }


   @Test
   @DisplayName("existsByDocumentId() - must check if activity logs exist (only by organization id)")
   void existsByDocumentId() {
      // id's - setup
      UUID organizationId = UUID.randomUUID();
 
      // stubs
      when(this.activityLogRepository.existsByOrganizationId(organizationId)).thenReturn(true);
 
      // act and assert
      boolean result = this.activityLogService.existsByDocumentId(organizationId, "o");
      assertThat(result).isTrue();
 
      // checks
      verify(this.activityLogRepository, times(1)).existsByOrganizationId(organizationId);
   }


   @Test
   @DisplayName("deleteByDocumentId() - must delete all activity logs (only by organization id)")
   void deleteByDocumentIdOrganization() {
      // id's - setup
      UUID organizationId = UUID.randomUUID();
 
      // act
      assertThatCode(() -> this.activityLogService.deleteByDocumentId(organizationId, "o"))
         .doesNotThrowAnyException();
 
      // checks and assert (in this case)
      verify(this.activityLogRepository, times(1)).deleteAllByOrganizationId(organizationId);
   }


   @Test
   @DisplayName("deleteActivityLog() - must delete an activity log")
   void deleteActivityLog() {
      // id's - setup
      String activityLogId = UUID.randomUUID().toString();
 
      // activity log - setup
      ActivityLogDocument activityLogDocument = new ActivityLogDocument.Builder()
         .id(activityLogId)
         .build();
 
      // stubs
      when(this.activityLogRepository.findById(activityLogId)).thenReturn(Optional.of(activityLogDocument));
 
      // act
      assertThatCode(() -> this.activityLogService.deleteActivityLog(activityLogId))
         .doesNotThrowAnyException();
 
      // checks and assert (in this case)
      verify(this.activityLogValidator, times(1)).idValidate(activityLogId);
      verify(this.activityLogRepository, times(1)).findById(activityLogId);
      verify(this.activityLogRepository, times(1)).deleteById(activityLogId);
   }

}