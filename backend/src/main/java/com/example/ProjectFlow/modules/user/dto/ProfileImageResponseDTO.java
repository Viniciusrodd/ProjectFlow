
// packages
package com.example.ProjectFlow.modules.user.dto;

// imports
import java.time.LocalDateTime;

// import documents
import com.example.ProjectFlow.modules.user.document.ProfileImagesDocument;


public record ProfileImageResponseDTO (

   String id,
   Long userId,
   String fileName,
   String mimeType,
   Long size,
   LocalDateTime uploadDate

) {

   public static ProfileImageResponseDTO get(ProfileImagesDocument document) {
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