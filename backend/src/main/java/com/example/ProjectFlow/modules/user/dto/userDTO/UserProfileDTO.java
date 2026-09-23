
// packages
package com.example.ProjectFlow.modules.user.dto.userDTO;

// imports
import java.util.UUID;


public record UserProfileDTO (

   UUID id,
   String email,
   String name,
   String profileImageId

) {}