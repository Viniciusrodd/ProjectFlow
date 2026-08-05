
// packages
package com.example.ProjectFlow.modules.auth.dto;

// import DTO
import com.example.ProjectFlow.modules.user.dto.UserDTO;


public record LoginResponseDTO (

   Long id,
   String name,
   String email,
   String token

) {

   public static LoginResponseDTO get(UserDTO document, String token) {
      return new LoginResponseDTO(
         document.id(),
         document.name(),
         document.email(),
         token
      );
   }

}