
// packages
package com.example.ProjectFlow.modules.project.dto;

// imports
import java.time.LocalDateTime;
import java.util.UUID;

// import document
import com.example.ProjectFlow.modules.project.document.ProjectImageDocument;


public record ProjectImageResponseDTO (

   String id,
   UUID projectId,
   String fileName,
   String mimeType,
   Long size,
   LocalDateTime uploadDate   

) {

   public static ProjectImageResponseDTO get(ProjectImageDocument document) {
      return new ProjectImageResponseDTO(
         document.getId(),
         document.getProjectId(),
         document.getFileName(),
         document.getMimeType(),
         document.getSize(),
         document.getUploadDate()
      );
   }

}