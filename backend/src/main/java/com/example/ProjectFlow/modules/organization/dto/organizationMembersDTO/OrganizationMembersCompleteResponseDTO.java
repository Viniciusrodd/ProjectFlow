
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;
import com.example.ProjectFlow.modules.organization.dto.organizationDTO.OrganizationResponseDTO;
// import entity
import com.example.ProjectFlow.modules.organization.entity.OrganizationMembersEntity;


public record OrganizationMembersCompleteResponseDTO (

   UUID id,
   UserProfileDTO user,
   OrganizationResponseDTO organization,
   RoleEnum role,
   LocalDateTime joinedAt

) {
 
   public static OrganizationMembersCompleteResponseDTO get(OrganizationMembersEntity document) {
      return new OrganizationMembersCompleteResponseDTO(
         document.getId(),
         UserProfileDTO.get(document.getUser()),
         OrganizationResponseDTO.get(document.getOrganization()),
         document.getRole(),
         document.getJoinedAt()
      );
   }

}