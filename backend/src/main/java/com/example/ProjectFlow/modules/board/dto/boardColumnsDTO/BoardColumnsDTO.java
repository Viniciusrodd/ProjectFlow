
// packages
package com.example.ProjectFlow.modules.board.dto.boardColumnsDTO;

// imports
import java.util.UUID;


public record BoardColumnsDTO (

   UUID boardId,
   String name,
   Integer position,
   String color

) {}