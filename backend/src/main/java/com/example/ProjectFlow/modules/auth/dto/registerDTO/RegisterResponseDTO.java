
// packages
package com.example.ProjectFlow.modules.auth.dto.registerDTO;

// imports
import java.time.LocalDateTime;
import java.util.UUID;


public record RegisterResponseDTO (

   UUID id,
   String name,
   String email,
   LocalDateTime createdAt

) {}