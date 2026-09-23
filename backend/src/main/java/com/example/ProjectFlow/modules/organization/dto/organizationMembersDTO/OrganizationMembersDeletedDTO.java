
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;


public record OrganizationMembersDeletedDTO (

   UUID organizationId,
   UUID userId,
   RoleEnum role,
   LocalDateTime joinedAt,
   LocalDateTime deletedAt

) {}