
// packages
package com.example.ProjectFlow.modules.project.dto.projectMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.project.enums.RoleEnum;


public record ProjectMembersResponseDTO (

   UUID projectId,
   UUID userId,
   RoleEnum role,
   LocalDateTime joinedAt

) {}