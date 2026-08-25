
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationMembersDTO;

// imports
import java.util.UUID;


public record OrganizationMembersDTO (

   UUID organizationId,
   UUID userId,
   String role

) {}