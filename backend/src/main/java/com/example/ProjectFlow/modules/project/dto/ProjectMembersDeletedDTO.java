
// packages
package com.example.ProjectFlow.modules.project.dto;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.project.enums.RoleEnum;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;


public record ProjectMembersDeletedDTO (

   UUID projectId,
   UUID userId,
   RoleEnum role,
   LocalDateTime joinedAt,
   LocalDateTime deletedAt

) {

   public static ProjectMembersDeletedDTO get(ProjectMembersEntity document) {
      return new ProjectMembersDeletedDTO(
         document.getProjectId(),
         document.getUserId(),
         document.getRole(),
         document.getJoinedAt(),
         document.getDeletedAt()
      );
   }

}