
// packages
package com.example.ProjectFlow.modules.project.dto.projectMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.project.enums.RoleEnum;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;

// import entity
import com.example.ProjectFlow.modules.project.entity.ProjectMembersEntity;


public record MemberByProjectResponseDTO (

   UUID id,
   UserProfileDTO user,
   RoleEnum role,
   LocalDateTime joinedAt

) {

   public static MemberByProjectResponseDTO get(ProjectMembersEntity document) {
      return new MemberByProjectResponseDTO(
         document.getId(),
         UserProfileDTO.get(document.getUser()),
         document.getRole(),
         document.getJoinedAt()
      );
   }

}