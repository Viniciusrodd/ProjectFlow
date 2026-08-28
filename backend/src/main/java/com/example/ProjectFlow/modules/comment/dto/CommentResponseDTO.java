
// packages
package com.example.ProjectFlow.modules.comment.dto;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.comment.entity.CommentEntity;


public record CommentResponseDTO (

   UUID id,
   UUID taskId,
   UUID authorId,
   String content
   
) {

   public static CommentResponseDTO get(CommentEntity document) {
      return new CommentResponseDTO(
         document.getId(),
         document.getTask().getId(),
         document.getAuthor().getId(),
         document.getContent()
      );
   }
   
}