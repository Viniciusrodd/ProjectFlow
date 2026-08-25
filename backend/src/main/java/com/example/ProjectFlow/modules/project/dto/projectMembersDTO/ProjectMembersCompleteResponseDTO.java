
// packages
package com.example.ProjectFlow.modules.project.dto.projectMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.project.enums.RoleEnum;
import com.example.ProjectFlow.modules.project.dto.projectDTO.ProjectResponseDTO;
// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserProfileDTO;


public record ProjectMembersCompleteResponseDTO (

   UUID id,
   ProjectResponseDTO project,
   UserProfileDTO user,
   RoleEnum role,
   LocalDateTime joinedAt

) {

   public static ProjectMembersCompleteResponseDTO get(ProjectMembersEntity document) {
      return new ProjectMembersCompleteResponseDTO(
         document.getId(),
         ProjectResponseDTO.get(document.getProject()),
         UserProfileDTO.get(document.getUser()),
         document.getRole(),
         document.getJoinedAt()
      );
   }

}