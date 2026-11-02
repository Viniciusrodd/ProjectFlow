
// packages
package com.example.ProjectFlow.modules.attachment.integrationTests.attachmentController;

// imports
import java.util.UUID;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 
// mockito imports
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.springframework.security.oauth2.jwt.JwtDecoder;
 
// spring imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 
// import controller
import com.example.ProjectFlow.modules.attachment.controller.AttachmentController;
 
// import services
import com.example.ProjectFlow.modules.attachment.service.AttachmentService;
 
// import mapper
import com.example.ProjectFlow.modules.attachment.mapper.AttachmentMapper;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;


@WebMvcTest(AttachmentController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - AttachmentController")
public class AttachmentBadTests {

   // for reqs http simulation
   @Autowired
   private MockMvc mockMvc;
   
   // mock's for disable security filter in test
   @MockitoBean
   private JwtDecoder jwtDecoder;
   @MockitoBean
   private JwtFilter jwtFilter;
 
   // replaces real services and mapper for spring context mock
   @MockitoBean 
   private AttachmentService attachmentService;
   @MockitoBean 
   private AttachmentMapper attachmentMapper;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH + "/task";


   @Test
   @DisplayName("POST - /task/{taskId}/attachment/{uploadedBy} - should return 500 when file processing fails")
   void uploadTaskAttachment() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      UUID uploadedBy = UUID.randomUUID();
      MockMultipartFile file = new MockMultipartFile(
         "file", "document.pdf", MediaType.APPLICATION_PDF_VALUE, "fake-attachment-content".getBytes()
      );
 
      // error message
      String errorMessage = "Erro interno: Falha ao processar anexo de tarefa";
 
      // stubs
      when(this.attachmentService.uploadAttachment(any(UUID.class), any(UUID.class), any()))
         .thenThrow(MultiExceptions.internal(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         multipart(this.BASE_PATH + "/" + taskId + "/attachment/" + uploadedBy)
         .file(file)
      )
         .andExpect(status().isInternalServerError())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(500))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.attachmentService).uploadAttachment(any(UUID.class), any(UUID.class), any());
   }
 
 
   @Test
   @DisplayName("GET - /task/attachment/{id} - should return 404 when attachment does not exist")
   void getTaskAttachment() throws Exception {
      // attachment id
      String attachmentId = "attachment123";
 
      // error message
      String errorMessage = "Recurso não encontrado: Anexo de tarefa não existe";
 
      // stubs
      when(this.attachmentService.getTaskAttachmentById(attachmentId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/attachment/" + attachmentId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.attachmentService).getTaskAttachmentById(attachmentId);
      
      // no checks
      verifyNoInteractions(this.attachmentMapper);
   }
 
 
   @Test
   @DisplayName("GET - /task/attachment/{id}/download - should return 404 when attachment does not exist")
   void getTaskAttachmentData() throws Exception {
      // attachment id
      String attachmentId = "attachment123";
 
      // error message
      String errorMessage = "Recurso não encontrado: Anexo de tarefa não existe";
 
      // stubs
      when(this.attachmentService.getTaskAttachmentById(attachmentId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/attachment/" + attachmentId + "/download")
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.attachmentService).getTaskAttachmentById(attachmentId);
   }
 
 
   @Test
   @DisplayName("GET - /task/{taskId}/attachments - should return 404 when task has no attachments")
   void getAllTaskAttachments() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Anexos de tarefa não existem";
 
      // stubs
      when(this.attachmentService.getAllTaskAttachments(taskId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + taskId + "/attachments")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.attachmentService).getAllTaskAttachments(taskId);
   }
 
 
   @Test
   @DisplayName("DELETE - /task/{taskId}/attachments - should return 404 when task has no attachments")
   void deleteAttachmentsByTaskId() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Anexos de tarefa não existem";
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage)).when(this.attachmentService).deleteByTaskId(taskId);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/" + taskId + "/attachments")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.attachmentService).deleteByTaskId(taskId);
   }
 
 
   @Test
   @DisplayName("DELETE - /task/attachment/{id} - should return 404 when attachment does not exist")
   void deleteTaskAttachment() throws Exception {
      // attachment id
      String attachmentId = "attachment123";
 
      // error message
      String errorMessage = "Recurso não encontrado: Anexo de tarefa não existe";
 
      // stubs
      doThrow(MultiExceptions.notFound(errorMessage)).when(this.attachmentService).deleteTaskAttachment(attachmentId);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/attachment/" + attachmentId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.attachmentService).deleteTaskAttachment(attachmentId);
   }

}