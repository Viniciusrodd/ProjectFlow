
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationDTO;

// imports
import java.util.UUID;


public record OrganizationResponseDTO (

   UUID id,
   UUID ownerId,
   String name,
   String description

) {}