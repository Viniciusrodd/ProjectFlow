
// packages
package com.example.ProjectFlow.modules.comment.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.comment.entity.CommentEntity;

// import DTOs
import com.example.ProjectFlow.modules.comment.dto.CommentResponseDTO;
import com.example.ProjectFlow.modules.comment.dto.CommentDeleteDTO;


@Component 
public class CommentMapper {
 
   // from CommentEntity to CommentResponseDTO
   public CommentResponseDTO toCommentResponseDTO(CommentEntity entity) {
      return new CommentResponseDTO(
         entity.getId(),
         entity.getTask().getId(),
         entity.getAuthor().getId(),
         entity.getContent()
      );
   }


   // from CommentEntity to CommentDeleteDTO
   public CommentDeleteDTO toCommentDeleteDTO(CommentEntity entity) {
      return new CommentDeleteDTO(
         entity.getId(),
         entity.getTask().getId(),
         entity.getAuthor().getId(),
         entity.getContent(),
         entity.getDeletedAt()
      );
   }

}