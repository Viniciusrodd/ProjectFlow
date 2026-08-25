
// packages
package com.example.ProjectFlow.modules.auth.dto.loginDTO;

// imports
import java.util.UUID;

// import DTO
import com.example.ProjectFlow.modules.user.dto.UserDTO;


public record LoginResponseDTO (

   UUID id,
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