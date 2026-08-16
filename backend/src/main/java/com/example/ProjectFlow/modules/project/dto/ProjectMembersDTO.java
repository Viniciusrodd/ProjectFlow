
// packages
package com.example.ProjectFlow.modules.project.dto;

// imports
import java.util.UUID;


public record ProjectMembersDTO (

   UUID projectId,
   UUID userId,
   String role

) {}