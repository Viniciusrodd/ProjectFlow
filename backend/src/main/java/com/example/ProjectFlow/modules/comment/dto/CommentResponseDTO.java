
// packages
package com.example.ProjectFlow.modules.comment.dto;

// imports
import java.util.UUID;


public record CommentResponseDTO (

   UUID id,
   UUID taskId,
   UUID authorId,
   String content
   
) {}