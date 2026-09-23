
// packages
package com.example.ProjectFlow.modules.comment.dto;

// imports
import java.util.UUID;
import java.time.LocalDateTime;


public record CommentDeleteDTO (

   UUID id,
   UUID taskId,
   UUID authorId,
   String content,
   LocalDateTime deletedAt

) {}