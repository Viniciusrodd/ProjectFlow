
// packages
package com.example.ProjectFlow.modules.organization.dto;

// imports
import java.util.UUID;


public record OrganizationDTO (

   UUID ownerId,
   String name,
   String description

) {}