
// packages
package com.example.ProjectFlow.modules.board.dto.boardDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;


public record BoardDeletedDTO (

   UUID id,
   UUID projectId,
   String name,
   LocalDateTime deletedAt

) {}