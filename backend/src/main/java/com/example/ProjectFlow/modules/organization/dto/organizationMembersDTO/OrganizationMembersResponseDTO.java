
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;


public record OrganizationMembersResponseDTO (

   UUID id,
   UUID organizationId,
   UUID userId,
   RoleEnum role,
   LocalDateTime joinedAt

) {}