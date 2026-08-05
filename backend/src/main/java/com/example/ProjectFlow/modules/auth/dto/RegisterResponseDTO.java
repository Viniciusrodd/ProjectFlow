
// packages
package com.example.ProjectFlow.modules.auth.dto;

// imports
import java.time.LocalDateTime;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;


public record RegisterResponseDTO (

   Long id,
   String name,
   String email,
   LocalDateTime createdAt

) {

   public static RegisterResponseDTO get(UserEntity document) {
      return new RegisterResponseDTO(
         document.getId(),
         document.getName(),
         document.getEmail(),
         document.getCreatedAt()
      );
   }

}