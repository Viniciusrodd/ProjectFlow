
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationImageDTO;

// imports
import java.time.LocalDateTime;
import java.util.UUID;


public record OrganizationImageResponseDTO (

   String id,
   UUID organizationId,
   String fileName,
   String mimeType,
   Long size,
   LocalDateTime uploadDate   

) {}