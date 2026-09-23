
// packages
package com.example.ProjectFlow.modules.project.dto.projectDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enums
import com.example.ProjectFlow.modules.project.enums.StatusEnum;


public record ProjectDeletedDTO (

   UUID id,
   UUID organizationId,
   UUID ownerId,
   String name,
   String description,
   StatusEnum status,
   LocalDateTime deletedAt

) {}