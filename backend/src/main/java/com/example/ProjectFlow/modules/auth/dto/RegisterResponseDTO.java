
// packages
package com.example.ProjectFlow.modules.auth.dto;

// imports
import java.time.LocalDateTime;


public record RegisterResponseDTO (

   Long id,
   String name,
   String email,
   LocalDateTime createdAt

) {}