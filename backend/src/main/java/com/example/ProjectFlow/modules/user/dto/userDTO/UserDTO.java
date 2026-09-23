
// packages
package com.example.ProjectFlow.modules.user.dto.userDTO;

// imports
import java.util.UUID;


public record UserDTO (
   
   UUID id,
   String email,
   String name,
   String password

) {}