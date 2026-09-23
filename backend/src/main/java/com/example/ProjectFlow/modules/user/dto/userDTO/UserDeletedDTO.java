
// packages
package com.example.ProjectFlow.modules.user.dto.userDTO;

// imports
import java.time.LocalDateTime;
import java.util.UUID;


public record UserDeletedDTO (

   UUID id,
   String name,
   String email,
   LocalDateTime deletedAt

) {}