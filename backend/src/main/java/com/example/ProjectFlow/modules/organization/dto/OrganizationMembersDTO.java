
// packages
package com.example.ProjectFlow.modules.organization.dto;

// imports
import java.util.UUID;

// import enum
import com.example.ProjectFlow.modules.organization.enums.RoleEnum;


public record OrganizationMembersDTO (

   UUID organizationId,
   UUID userId,
   RoleEnum role

) {}