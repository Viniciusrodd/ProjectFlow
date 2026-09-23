
// packages
package com.example.ProjectFlow.modules.organization.dto.organizationDTO;

// imports
import java.time.LocalDateTime;
import java.util.UUID;


public record OrganizationDeletedDTO (

   UUID id,
   UUID ownerId,
   String name,
   String description,
   LocalDateTime deletedAt

) {}