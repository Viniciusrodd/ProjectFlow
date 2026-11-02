
// packages
package com.example.ProjectFlow.modules.comment.integrationTests.commentController;

// imports
import java.time.LocalDateTime;
import java.util.List;
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
import com.example.ProjectFlow.modules.comment.dto.CommentDeleteDTO;
import com.example.ProjectFlow.modules.comment.dto.CommentResponseDTO;

// import constants
import com.example.ProjectFlow.common.constants.ApiConstants;

// import JWT
import com.example.ProjectFlow.security.JWT.JwtFilter;


@WebMvcTest(CommentController.class) // focus only in this controller
@AutoConfigureMockMvc(addFilters = false) // disable security filter in test
@DisplayName("Integration tests (Happy path) - CommentController")
public class CommentHappyTests {

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
   @DisplayName("POST - /comment - should return created comment and status 201")
   void createComment() throws Exception {
      // data - setup
      UUID taskId = UUID.randomUUID();
      UUID authorId = UUID.randomUUID();
      CommentDTO commentDTO = new CommentDTO(taskId, authorId, "This looks good!");
 
      // comment response data
      CommentResponseDTO commentResponseDTO = new CommentResponseDTO(
         UUID.randomUUID(), taskId, authorId, commentDTO.content()
      );
 
      // stubs
      when(this.commentService.create(commentDTO)).thenReturn(commentResponseDTO);
 
      // act and assert
      this.mockMvc.perform(
         post(this.BASE_PATH + "/comment")
         .contentType(MediaType.APPLICATION_JSON)
         .content(this.objectMapper.writeValueAsString(commentDTO))
      )
         .andExpect(status().isCreated())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(201))
         .andExpect(jsonPath("$.data.id").value(commentResponseDTO.id().toString()))
         .andExpect(jsonPath("$.data.taskId").value(taskId.toString()))
         .andExpect(jsonPath("$.data.authorId").value(authorId.toString()))
         .andExpect(jsonPath("$.data.content").value(commentResponseDTO.content()));
 
      // checks
      verify(this.commentService).create(commentDTO);
   }
 
 
   @Test
   @DisplayName("GET - /comments - should return all comments and status 200")
   void getAllComments() throws Exception {
      // comments data
      List<CommentResponseDTO> comments = List.of(
         new CommentResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "First comment"
         ),
         new CommentResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "Second comment"
         )
      );
 
      // stubs
      when(this.commentService.getAll()).thenReturn(comments);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/comments")
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(comments.size()))
         .andExpect(jsonPath("$.data[0].id").value(comments.get(0).id().toString()))
         .andExpect(jsonPath("$.data[0].content").value(comments.get(0).content()))
         .andExpect(jsonPath("$.data[1].id").value(comments.get(1).id().toString()))
         .andExpect(jsonPath("$.data[1].content").value(comments.get(1).content()));
 
      // checks
      verify(this.commentService).getAll();
   }
 
 
   @Test
   @DisplayName("GET - /comment/{id} - should return the comment and status 200")
   void getCommentById() throws Exception {
      // comment data
      UUID commentId = UUID.randomUUID();
      CommentResponseDTO comment = new CommentResponseDTO(
         commentId, UUID.randomUUID(), UUID.randomUUID(), "Comment content"
      );
 
      // stubs
      when(this.commentService.getById(commentId)).thenReturn(comment);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/comment/" + commentId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(comment.id().toString()))
         .andExpect(jsonPath("$.data.taskId").value(comment.taskId().toString()))
         .andExpect(jsonPath("$.data.authorId").value(comment.authorId().toString()))
         .andExpect(jsonPath("$.data.content").value(comment.content()));
 
      // checks
      verify(this.commentService).getById(commentId);
   }
 
 
   @Test
   @DisplayName("GET - /comments/task/{taskId} - should return comments of the task and status 200")
   void getByTaskId() throws Exception {
      // task id
      UUID taskId = UUID.randomUUID();
 
      // comments data
      List<CommentResponseDTO> comments = List.of(
         new CommentResponseDTO(
            UUID.randomUUID(), taskId, UUID.randomUUID(), "First comment"
         ),
         new CommentResponseDTO(
            UUID.randomUUID(), taskId, UUID.randomUUID(), "Second comment"
         )
      );
 
      // stubs
      when(this.commentService.getByTaskId(taskId)).thenReturn(comments);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/comments/task/" + taskId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(comments.size()))
         .andExpect(jsonPath("$.data[0].taskId").value(taskId.toString()))
         .andExpect(jsonPath("$.data[1].taskId").value(taskId.toString()));
 
      // checks
      verify(this.commentService).getByTaskId(taskId);
   }
 
 
   @Test
   @DisplayName("GET - /comments/author/{authorId} - should return comments of the author and status 200")
   void getByAuthorId() throws Exception {
      // author id
      UUID authorId = UUID.randomUUID();
 
      // comments data
      List<CommentResponseDTO> comments = List.of(
         new CommentResponseDTO(
            UUID.randomUUID(), UUID.randomUUID(), authorId, "First comment"
         )
      );
 
      // stubs
      when(this.commentService.getByAuthorId(authorId)).thenReturn(comments);
 
      // act and assert
      this.mockMvc.perform(
         get(this.BASE_PATH + "/comments/author/" + authorId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.length()").value(comments.size()))
         .andExpect(jsonPath("$.data[0].authorId").value(authorId.toString()));
 
      // checks
      verify(this.commentService).getByAuthorId(authorId);
   }
 
 
   @Test
   @DisplayName("PUT - /comment/{id}/content - should return status 200 when updating the content")
   void updateCommentContent() throws Exception {
      // data - setup
      UUID commentId = UUID.randomUUID();
      String newContent = "Updated comment content";
 
      // updated comment data
      CommentResponseDTO updatedComment = new CommentResponseDTO(
         commentId, UUID.randomUUID(), UUID.randomUUID(), newContent
      );
 
      // stubs
      when(this.commentService.updateContent(commentId, newContent)).thenReturn(updatedComment);
 
      // act and assert
      this.mockMvc.perform(
         put(this.BASE_PATH + "/comment/" + commentId + "/content")
         .param("c", newContent)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(updatedComment.id().toString()))
         .andExpect(jsonPath("$.data.content").value(updatedComment.content()));
 
      // checks
      verify(this.commentService).updateContent(commentId, newContent);
   }
 
 
   @Test
   @DisplayName("DELETE - /comment/{id} - should return the deleted comment and status 200")
   void deleteComment() throws Exception {
      // data - setup
      UUID commentId = UUID.randomUUID();
      CommentDeleteDTO deletedComment = new CommentDeleteDTO(
         commentId, UUID.randomUUID(), UUID.randomUUID(), "Comment to delete", LocalDateTime.now()
      );
 
      // stubs
      when(this.commentService.delete(commentId)).thenReturn(deletedComment);
 
      // act and assert
      this.mockMvc.perform(
         delete(this.BASE_PATH + "/comment/" + commentId)
         .contentType(MediaType.APPLICATION_JSON)
      )
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.success").value(true))
         .andExpect(jsonPath("$.statusCode").value(200))
         .andExpect(jsonPath("$.data.id").value(deletedComment.id().toString()))
         .andExpect(jsonPath("$.data.taskId").value(deletedComment.taskId().toString()))
         .andExpect(jsonPath("$.data.authorId").value(deletedComment.authorId().toString()))
         .andExpect(jsonPath("$.data.content").value(deletedComment.content()))
         .andExpect(jsonPath("$.data.deletedAt").isNotEmpty());
 
      // checks
      verify(this.commentService).delete(commentId);
   }

}