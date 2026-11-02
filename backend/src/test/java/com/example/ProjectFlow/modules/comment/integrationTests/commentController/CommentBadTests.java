
// packages
package com.example.ProjectFlow.modules.comment.integrationTests.commentController;

// imports
import java.util.UUID;
 
// jackson imports
import tools.jackson.databind.ObjectMapper;
 
// JUnit imports
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 
// mockito imports
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.oauth2.jwt.JwtDecoder;
 
// spring imports
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 
// import controller
import com.example.ProjectFlow.modules.comment.controller.CommentController;
 
// import services
import com.example.ProjectFlow.modules.comment.service.CommentService;
 
// import DTOs
import com.example.ProjectFlow.modules.comment.dto.CommentDTO;
 
// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;
 
// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;
 
// import exceptions
import com.example.ProjectFlow.exception.MultiExceptions;

 
@WebMvcTest(CommentController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Bad path) - CommentController")
public class CommentBadTests {
   
   // for reqs http simulation
   @Autowired
   private MockMvc mockMvc;
   
   // to convert java objects <-> json
   @Autowired
   private ObjectMapper objectMapper;
   
   // mock's for disable security filter in test
   @MockitoBean
   private JwtDecoder jwtDecoder;
   @MockitoBean
   private JwtFilter jwtFilter;
 
   // replaces real services for spring context mock
   @MockitoBean 
   private CommentService commentService;
   
   // path
   private final String BASE_PATH = ApiConstants.BASE_API_PATH;


   @Test
   @DisplayName("POST - /comment - should return 404 when task does not exist")
   void createComment() throws Exception {
      // data - setup
      CommentDTO commentDTO = new CommentDTO(
         UUID.randomUUID(), UUID.randomUUID(), "Comment content"
      );
 
      // error message
      String errorMessage = "Recurso não encontrado: Tarefa não existe";
 
      // stubs
      when(this.commentService.create(commentDTO)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/comment")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(commentDTO))
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.commentService).create(commentDTO);
   }
 
 
   @Test
   @DisplayName("GET - /comments - should return 404 when there are no comments")
   void getAllComments() throws Exception {
      // error message
      String errorMessage = "Recurso não encontrado: Comentários não existem";
 
      // stubs
      when(this.commentService.getAll()).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/comments")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.commentService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /comment/{id} - should return 404 when comment does not exist")
   void getCommentById() throws Exception {
      // comment id
      UUID commentId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Comentário não existe";
 
      // stubs
      when(this.commentService.getById(commentId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/comment/" + commentId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.commentService).getById(commentId);
   }
 
 
   @Test
   @DisplayName("GET - /comments/task/{taskId} - should return 404 when task has no comments")
   void getByTaskId() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Comentários não existem";
 
      // stubs
      when(this.commentService.getByTaskId(taskId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/comments/task/" + taskId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.commentService).getByTaskId(taskId);
   }
 
 
   @Test
   @DisplayName("GET - /comments/author/{authorId} - should return 404 when author has no comments")
   void getByAuthorId() throws Exception {
      // author id
      UUID authorId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Comentários não existem";
 
      // stubs
      when(this.commentService.getByAuthorId(authorId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/comments/author/" + authorId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.commentService).getByAuthorId(authorId);
   }
 
 
   @Test
   @DisplayName("PUT - /comment/{id}/content - should return 404 when comment does not exist")
   void updateCommentContent() throws Exception {
      // data - setup
      UUID commentId = UUID.randomUUID();
      String newContent = "Updated comment content";
 
      // error message
      String errorMessage = "Recurso não encontrado: Comentário não existe";
 
      // stubs
      when(this.commentService.updateContent(commentId, newContent)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/comment/" + commentId + "/content")
         .param("c", newContent)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.commentService).updateContent(commentId, newContent);
   }
 
 
   @Test
   @DisplayName("DELETE - /comment/{id} - should return 404 when comment does not exist")
   void deleteComment() throws Exception {
      // comment id
      UUID commentId = UUID.randomUUID();
 
      // error message
      String errorMessage = "Recurso não encontrado: Comentário não existe";
 
      // stubs
      when(this.commentService.delete(commentId)).thenThrow(MultiExceptions.notFound(errorMessage));
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/comment/" + commentId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isNotFound())
         .andExpect(jsonPath("$.success").value(false))
         .andExpect(jsonPath("$.statusCode").value(404))
         .andExpect(jsonPath("$.message").value(errorMessage))
         .andExpect(jsonPath("$.data").isEmpty());
 
      // check
      verify(this.commentService).delete(commentId);
   }

}