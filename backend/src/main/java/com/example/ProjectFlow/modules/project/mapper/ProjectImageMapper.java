
// packages
package com.example.ProjectFlow.modules.project.mapper;

// imports
import org.springframework.stereotype.Component;

// import document
import com.example.ProjectFlow.modules.project.document.ProjectImageDocument;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectImageDTO.ProjectImageResponseDTO;


@Component 
public class ProjectImageMapper {
 
   // from ProjectImageDocument to ProjectImageResponseDTO
   public ProjectImageResponseDTO toProjectImageResponseDTO(ProjectImageDocument document) {
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