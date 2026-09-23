
// packages
package com.example.ProjectFlow.modules.board.dto.boardDTO;

// imports
import java.util.UUID;


public record BoardResponseDTO (

   UUID id,
   UUID projectId,
   String name
   
) {}