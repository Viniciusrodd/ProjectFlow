
// packages
package com.example.ProjectFlow.modules.project.dto.projectMembersDTO;

// imports
import java.util.UUID;
import java.time.LocalDateTime;

// import enum
import com.example.ProjectFlow.modules.project.enums.RoleEnum;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;


public record MemberByProjectResponseDTO (

   UUID id,
   UserProfileDTO user,
   RoleEnum role,
   LocalDateTime joinedAt

) {}