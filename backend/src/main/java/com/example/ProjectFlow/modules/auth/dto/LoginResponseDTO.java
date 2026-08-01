
// packages
package com.example.ProjectFlow.modules.auth.dto;


public record LoginResponseDTO (

   String token,
   String email,
   String name,
   Long id

) {}