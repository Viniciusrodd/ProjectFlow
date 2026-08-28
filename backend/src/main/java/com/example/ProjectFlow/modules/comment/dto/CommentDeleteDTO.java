
// packages
package com.example.ProjectFlow.modules.comment.dto;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import entity
import com.example.ProjectFlow.modules.comment.entity.CommentEntity;


public record CommentDeleteDTO (

   UUID id,
   UUID taskId,
   UUID authorId,
   String content,
   LocalDateTime deletedAt

) {
   
   public static CommentDeleteDTO get(CommentEntity document) {
      return new CommentDeleteDTO(
         document.getId(),
         document.getTask().getId(),
         document.getAuthor().getId(),
         document.getContent(),
         document.getDeletedAt()
      );
   }

}