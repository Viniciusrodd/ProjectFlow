
// packages
package com.example.ProjectFlow.modules.project.dto.projectImageDTO;

// imports
import java.time.LocalDateTime;
import java.util.UUID;


public record ProjectImageResponseDTO (

   String id,
   UUID projectId,
   String fileName,
   String mimeType,
   Long size,
   LocalDateTime uploadDate   

) {}