
// packages
package com.example.ProjectFlow.modules.auth.dto.loginDTO;

// imports
import java.util.UUID;


public record LoginResponseDTO (

   UUID id,
   String name,
   String email,
   String token

) {}