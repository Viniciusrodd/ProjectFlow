
// packages
package com.example.ProjectFlow.modules.user.dto.profileImageDTO;

// imports
import java.time.LocalDateTime;
import java.util.UUID;


public record ProfileImageResponseDTO (

   String id,
   UUID userId,
   String fileName,
   String mimeType,
   Long size,
   LocalDateTime uploadDate

) {}