
// packages
package com.example.ProjectFlow.modules.project.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectEntity;

// import DTOs
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectDeletedDTO;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectResponseDTO;


@Component 
public class ProjectMapper {

   // from ProjectEntity to ProjectResponseDTO
   public ProjectResponseDTO toProjectResponseDTO(ProjectEntity entity) {
      return new ProjectResponseDTO(
         entity.getId(),
         entity.getOrganizationId(),
         entity.getOwnerId(),
         entity.getName(),
         entity.getDescription(),
         entity.getStatus()
      );
   }


   // from ProjectEntity to ProjectDeletedDTO
   public ProjectDeletedDTO toProjectDeletedDTO(ProjectEntity entity) {
      return new ProjectDeletedDTO(
         entity.getId(),
         entity.getOrganizationId(),
         entity.getOwnerId(),
         entity.getName(),
         entity.getDescription(),
         entity.getStatus(),
         entity.getDeletedAt()
      );
   }

}