
// packages
package com.example.ProjectFlow.modules.user.dto;

// imports
import java.time.LocalDateTime;
import java.util.UUID;

// import documents
import com.example.ProjectFlow.modules.user.document.ProfileImageDocument;


public record ProfileImageResponseDTO (

   String id,
   UUID userId,
   String fileName,
   String mimeType,
   Long size,
   LocalDateTime uploadDate

) {

   public static ProfileImageResponseDTO get(ProfileImageDocument document) {
      return new ProfileImageResponseDTO(
         document.getId(),
         document.getUserId(),
         document.getFileName(),
         document.getMimeType(),
         document.getSize(),
         document.getUploadDate()
      );
   }

}