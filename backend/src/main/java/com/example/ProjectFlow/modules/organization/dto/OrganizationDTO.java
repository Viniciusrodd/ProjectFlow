
// packages
package com.example.ProjectFlow.modules.organization.dto;

// imports
import java.util.UUID;


public record OrganizationDTO (

   String name,
   String description,
   UUID ownerId   

) {}