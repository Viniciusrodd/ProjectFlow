
// packages
package com.example.ProjectFlow.modules.organization.dto;

// imports
import java.time.LocalDateTime;
import java.util.UUID;

// import document
import com.example.ProjectFlow.modules.organization.document.OrganizationImageDocument;


public record OrganizationImageResponseDTO (

   String id,
   UUID organizationId,
   String fileName,
   String mimeType,
   Long size,
   LocalDateTime uploadDate   

) {

   public static OrganizationImageResponseDTO get(OrganizationImageDocument document) {
      return new OrganizationImageResponseDTO(
         document.getId(),
         document.getOrganizationId(),
         document.getFileName(),
         document.getMimeType(),
         document.getSize(),
         document.getUploadDate()
      );
   }

}