
// packages
package com.example.ProjectFlow.modules.user.dto;

// imports
import java.time.LocalDateTime;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;


public record UserDeletedDTO (

   Long id,
   String name,
   String email,
   LocalDateTime deletedAt

) {

   public static UserDeletedDTO get(UserEntity document) {
      return new UserDeletedDTO(
         document.getId(),
         document.getName(),
         document.getEmail(),
         document.getDeletedAt()
      );
   }

}