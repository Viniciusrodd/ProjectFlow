
// packages
package com.example.ProjectFlow.modules.user.dto;

// imports
import java.time.LocalDateTime;


public record ProfileImageResponseDTO (

   String id,
   Long userId,
   String fileName,
   String mimeType,
   Long size,
   LocalDateTime uploadDate

) {}