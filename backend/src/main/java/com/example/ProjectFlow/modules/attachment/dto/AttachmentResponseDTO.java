
// packages
package com.example.ProjectFlow.modules.attachment.dto;

// imports
import java.time.LocalDateTime;
import java.util.UUID;


public record AttachmentResponseDTO (
   
   String id,
   UUID taskId,
   UUID uploadedBy,
   String fileName,
   String mimeType,
   Long size,
   LocalDateTime uploadDate

) {}