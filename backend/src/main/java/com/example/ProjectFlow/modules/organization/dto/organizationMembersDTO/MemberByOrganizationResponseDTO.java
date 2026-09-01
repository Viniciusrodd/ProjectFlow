
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;

// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;


public record MemberByOrganizationResponseDTO (

   UUID id,
   UserProfileDTO user,
   RoleEnum role,
   LocalDateTime joinedAt

) {
 
   public static MemberByOrganizationResponseDTO get(OrganizationMembersEntity document) {
      return new MemberByOrganizationResponseDTO(
         document.getId(),
         UserProfileDTO.get(document.getUser()),
         document.getRole(),
         document.getJoinedAt()
      );
   }

}