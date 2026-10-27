
// packages
package com.example.ProjectFlow.modules.activityLog.unitTests;

// imports
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataAccessResourceFailureException;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
 
// mockito imports
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
 
// import repository
import com.example.ProjectFlow.modules.activityLog.repository.ActivityLogRepository;
 
// import validator
import com.example.ProjectFlow.modules.activityLog.validator.ActivityLogValidator;
 
// import document
import com.example.ProjectFlow.modules.activityLog.document.ActivityLogDocument;
 
// import DTOs
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogDTO;
 
// import service
import com.example.ProjectFlow.modules.activityLog.service.ActivityLogService;
 
// import mapper
import com.example.ProjectFlow.modules.activityLog.mapper.ActivityLogMapper;
 
// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;
 
 
@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests (Bad path) - ActivityLogService")
public class ActivityLogBadTests {
   
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
   @DisplayName("create() - must throw when database fails")
   void create() {
      // DTO - setup
      ActivityLogDTO activityLogDTO = new ActivityLogDTO.Builder()
         .projectId(UUID.randomUUID())
         .action(ActivityActionEnum.TAREFA_ATUALIZADA)
         .description("Tarefa: task x, atualizada")
         .build();
 
      // stubs
      when(this.activityLogRepository.save(any(ActivityLogDocument.class)))
         .thenThrow(new DataAccessResourceFailureException("falha no mongo"));
 
      // act and assert
      assertThatThrownBy(() -> this.activityLogService.create(activityLogDTO))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Erro na criação do registro de atividade: falha no mongo");
 
      // checks
      verify(this.activityLogValidator, times(1)).actionValidate(activityLogDTO.action().toString());
      verify(this.activityLogValidator, times(1)).descriptionValidate(activityLogDTO.description());
      verify(this.activityLogRepository, times(1)).save(any(ActivityLogDocument.class));
 
      // no checks
      verifyNoInteractions(this.activityLogMapper);
   }


   @Test
   @DisplayName("getById() - must throw activity log not found")
   void getById() {
      // id's - setup
      String activityLogId = UUID.randomUUID().toString();
 
      // stubs
      when(this.activityLogRepository.findById(activityLogId)).thenReturn(Optional.empty());
 
      // act and assert
      assertThatThrownBy(() -> this.activityLogService.getById(activityLogId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Registro de atividade não existe");
 
      // checks
      verify(this.activityLogValidator, times(1)).idValidate(activityLogId);
      verify(this.activityLogRepository, times(1)).findById(activityLogId);
 
      // no checks
      verifyNoInteractions(this.activityLogMapper);
   }
 
 
   @Test
   @DisplayName("getDocumentById() - must throw activity log not found")
   void getDocumentById() {
      // id's - setup
      String activityLogId = UUID.randomUUID().toString();
 
      // stubs
      when(this.activityLogRepository.findById(activityLogId)).thenReturn(Optional.empty());
 
      // act and assert
      assertThatThrownBy(() -> this.activityLogService.getDocumentById(activityLogId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Registro de atividade não existe");
 
      // checks
      verify(this.activityLogValidator, times(1)).idValidate(activityLogId);
      verify(this.activityLogRepository, times(1)).findById(activityLogId);
   }
 
 
   @Test
   @DisplayName("existsById() - must throw when activity log does not exist")
   void existsById() {
      // id's - setup
      String activityLogId = UUID.randomUUID().toString();
 
      // stubs
      when(this.activityLogRepository.existsById(activityLogId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.activityLogService.existsById(activityLogId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Registro de atividade não existe");
 
      // checks
      verify(this.activityLogValidator, times(1)).idValidate(activityLogId);
      verify(this.activityLogRepository, times(1)).existsById(activityLogId);
   }


   @Test
   @DisplayName("getAllByDocumentId() - must throw when activity log list is empty")
   void getAllByDocumentId() {
      // id's - setup
      UUID projectId = UUID.randomUUID();
 
      // list - setup
      List<ActivityLogDocument> documentList = List.of();
 
      // stubs
      when(this.activityLogRepository.findByProjectId(projectId)).thenReturn(documentList);
 
      // act and assert
      assertThatThrownBy(() -> this.activityLogService.getAllByDocumentId(projectId, "p"))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Registros de atividade não existem");
 
      // checks
      verify(this.activityLogRepository, times(1)).findByProjectId(projectId);
 
      // no checks
      verifyNoInteractions(this.activityLogMapper);
   }


   @Test
   @DisplayName("existsByDocumentId() - must throw when activity logs do not exist")
   void existsByDocumentIdNotFound() {
      // id's - setup
      UUID taskId = UUID.randomUUID();
 
      // stubs
      when(this.activityLogRepository.existsByTaskId(taskId)).thenReturn(false);
 
      // act and assert
      assertThatThrownBy(() -> this.activityLogService.existsByDocumentId(taskId, "t"))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Registros de atividade não existem");
 
      // checks
      verify(this.activityLogRepository, times(1)).existsByTaskId(taskId);
   }


   @Test
   @DisplayName("deleteByDocumentId() - must throw when document type is invalid")
   void deleteByDocumentIdInvalidType() {
      // id's - setup
      UUID documentId = UUID.randomUUID();
 
      // act and assert
      assertThatThrownBy(() -> this.activityLogService.deleteByDocumentId(documentId, "x"))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Tipo de documento inválido");
 
      // no checks
      verifyNoInteractions(this.activityLogRepository);
   }
 
 
   @Test
   @DisplayName("deleteActivityLog() - must throw when activity log does not exist")
   void deleteActivityLog() {
      // id's - setup
      String activityLogId = UUID.randomUUID().toString();
 
      // stubs
      when(this.activityLogRepository.findById(activityLogId)).thenReturn(Optional.empty());
 
      // act and assert
      assertThatThrownBy(() -> this.activityLogService.deleteActivityLog(activityLogId))
         .isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Recurso não encontrado: Registro de atividade não existe");
 
      // checks
      verify(this.activityLogValidator, times(1)).idValidate(activityLogId);
      verify(this.activityLogRepository, times(1)).findById(activityLogId);
 
      // no checks
      verify(this.activityLogRepository, never()).deleteById(any());
   }

}