
// packages
package com.example.ProjectFlow.modules.project.dto;

// imports
import java.util.UUID;


public record ProjectDTO (

   UUID organizationId,
   UUID ownerId,
   String name,
   String description

) {}