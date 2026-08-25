
// packages
package com.example.ProjectFlow.modules.project.dto.projectMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.project.enums.RoleEnum;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;


public record ProjectMembersResponseDTO (

   UUID projectId,
   UUID userId,
   RoleEnum role,
   LocalDateTime joinedAt

) {

   public static ProjectMembersResponseDTO get(ProjectMembersEntity document) {
      return new ProjectMembersResponseDTO(
         document.getProjectId(),
         document.getUserId(),
         document.getRole(),
         document.getJoinedAt()
      );
   }

}