
// packages
package com.example.ProjectFlow.modules.attachment.integrationTests.attachmentController;

// imports
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

// mockito imports
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import controller
import com.example.ProjectFlow.modules.attachment.controller.AttachmentController;

// import services
import com.example.ProjectFlow.modules.attachment.service.AttachmentService;

// import DTOs
import com.example.ProjectFlow.modules.attachment.dto.AttachmentResponseDTO;

// import document
import com.example.ProjectFlow.modules.attachment.document.AttachmentDocument;

// import mapper
import com.example.ProjectFlow.modules.attachment.mapper.AttachmentMapper;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;


@WebMvcTest(AttachmentController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - AttachmentController")
public class AttachmentHappyTests {

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
   @DisplayName("POST - /task/{taskId}/attachment/{uploadedBy} - should return the uploaded attachment data and status 201")
   void uploadTaskAttachment() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      UUID uploadedBy = UUID.randomUUID();
      byte[] fileBytes = "fake-attachment-content".getBytes();
      MockMultipartFile file = new MockMultipartFile(
         "file", "document.pdf", MediaType.APPLICATION_PDF_VALUE, fileBytes
      );
 
      // upload response data
      AttachmentResponseDTO attachmentResponseDTO = new AttachmentResponseDTO(
         "attachment123", taskId, uploadedBy, "document.pdf",
         MediaType.APPLICATION_PDF_VALUE, (long) fileBytes.length, LocalDateTime.now()
      );
 
      // stubs
      when(this.attachmentService.uploadAttachment(any(UUID.class), any(UUID.class), any()))
         .thenReturn(attachmentResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         multipart(this.BASE_PATH + "/" + taskId + "/attachment/" + uploadedBy)
         .file(file)
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(attachmentResponseDTO.id()))
         .andExpect(jsonPath("$.data.taskId").value(taskId.toString()))
         .andExpect(jsonPath("$.data.uploadedBy").value(uploadedBy.toString()))
         .andExpect(jsonPath("$.data.fileName").value(attachmentResponseDTO.fileName()))
         .andExpect(jsonPath("$.data.mimeType").value(attachmentResponseDTO.mimeType()))
         .andExpect(jsonPath("$.data.size").value(attachmentResponseDTO.size()))
         .andExpect(jsonPath("$.data.uploadDate").isNotEmpty());
 
      // checks
      verify(this.attachmentService).uploadAttachment(any(UUID.class), any(UUID.class), any());
   }
 
 
   @Test
   @DisplayName("GET - /task/attachment/{id} - should return the attachment data and status 200")
   void getTaskAttachment() throws Exception {
      // attachment data
      String attachmentId = "attachment123";
      AttachmentDocument document = new AttachmentDocument.Builder()
         .taskId(UUID.randomUUID())
         .uploadedBy(UUID.randomUUID())
         .fileName("document.pdf")
         .mimeType(MediaType.APPLICATION_PDF_VALUE)
         .size(18L)
         .uploadDate(LocalDateTime.now())
         .binary("fake-attachment-content".getBytes())
         .build();
 
      // attachment response data
      AttachmentResponseDTO attachmentResponseDTO = new AttachmentResponseDTO(
         attachmentId, UUID.randomUUID(), UUID.randomUUID(), "document.pdf",
         MediaType.APPLICATION_PDF_VALUE, 18L, LocalDateTime.now()
      );
 
      // stubs
      when(this.attachmentService.getTaskAttachmentById(attachmentId)).thenReturn(document);
      when(this.attachmentMapper.toAttachmentResponseDTO(document)).thenReturn(attachmentResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/attachment/" + attachmentId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(attachmentResponseDTO.id()))
         .andExpect(jsonPath("$.data.fileName").value(attachmentResponseDTO.fileName()))
         .andExpect(jsonPath("$.data.mimeType").value(attachmentResponseDTO.mimeType()))
         .andExpect(jsonPath("$.data.size").value(attachmentResponseDTO.size()));
 
      // checks
      verify(this.attachmentService).getTaskAttachmentById(attachmentId);
      verify(this.attachmentMapper).toAttachmentResponseDTO(document);
   }
 
 
   @Test
   @DisplayName("GET - /task/attachment/{id}/download - should return the attachment bytes and status 200")
   void getTaskAttachmentData() throws Exception {
      // data - setup
      String attachmentId = "attachment123";
      byte[] fileBytes = "fake-attachment-content".getBytes();
      AttachmentDocument document = new AttachmentDocument.Builder()
         .taskId(UUID.randomUUID())
         .uploadedBy(UUID.randomUUID())
         .fileName("document.pdf")
         .mimeType(MediaType.APPLICATION_PDF_VALUE)
         .size((long) fileBytes.length)
         .uploadDate(LocalDateTime.now())
         .binary(fileBytes)
         .build();
 
      // stubs
      when(this.attachmentService.getTaskAttachmentById(attachmentId)).thenReturn(document);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/attachment/" + attachmentId + "/download")
         .accept(MediaType.APPLICATION_PDF)
      )
         .andExpect(status().isOk())
         .andExpect(content().contentType(MediaType.APPLICATION_PDF))
         .andExpect(header().string("Content-Disposition", "inline; filename=\"document.pdf\""))
         .andExpect(content().bytes(fileBytes));
 
      // checks
      verify(this.attachmentService).getTaskAttachmentById(attachmentId);
   }
 
 
   @Test
   @DisplayName("GET - /task/{taskId}/attachments - should return all attachments of the task and status 200")
   void getAllTaskAttachments() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // attachments data
      List<AttachmentResponseDTO> attachments = List.of(
         new AttachmentResponseDTO(
            "attachment1", taskId, UUID.randomUUID(), "doc1.pdf",
            MediaType.APPLICATION_PDF_VALUE, 100L, LocalDateTime.now()
         ),
         new AttachmentResponseDTO(
            "attachment2", taskId, UUID.randomUUID(), "doc2.png",
            MediaType.IMAGE_PNG_VALUE, 200L, LocalDateTime.now()
         )
      );
 
      // stubs
      when(this.attachmentService.getAllTaskAttachments(taskId)).thenReturn(attachments);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/" + taskId + "/attachments")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(attachments.size()))
         .andExpect(jsonPath("$.data[0].id").value(attachments.get(0).id()))
         .andExpect(jsonPath("$.data[0].fileName").value(attachments.get(0).fileName()))
         .andExpect(jsonPath("$.data[1].id").value(attachments.get(1).id()))
         .andExpect(jsonPath("$.data[1].fileName").value(attachments.get(1).fileName()));
 
      // checks
      verify(this.attachmentService).getAllTaskAttachments(taskId);
   }
 
 
   @Test
   @DisplayName("DELETE - /task/{taskId}/attachments - should return status 200 when deleting all attachments")
   void deleteAttachmentsByTaskId() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/" + taskId + "/attachments")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // checks
      verify(this.attachmentService).deleteByTaskId(taskId);
   }
 
 
   @Test
   @DisplayName("DELETE - /task/attachment/{id} - should return status 200 when deleting the attachment")
   void deleteTaskAttachment() throws Exception {
      // attachment id
      String attachmentId = "attachment123";
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/attachment/" + attachmentId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // checks
      verify(this.attachmentService).deleteTaskAttachment(attachmentId);
   }

}