
// packages
package com.example.ProjectFlow.modules.auth.dto;

// imports
import java.util.UUID;


public record AuthUserDTO (

   UUID id,
   String email,
   String name

) {}