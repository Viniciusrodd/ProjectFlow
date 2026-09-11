
// packages
package com.example.ProjectFlow.modules.user.mapper;

// imports
import org.springframework.stereotype.Component;

// import documents
import com.example.ProjectFlow.modules.user.document.ProfileImageDocument;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.profileImageDTO.ProfileImageResponseDTO;


@Component 
public class ProfileImageMapper {
 
   // from ProfileImageDocument to ProfileImageResponseDTO
   public ProfileImageResponseDTO toProfileImageResponseDTO(ProfileImageDocument document) {
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