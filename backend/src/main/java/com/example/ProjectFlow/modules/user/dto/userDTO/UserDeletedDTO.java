
// packages
package com.example.ProjectFlow.modules.user.dto.userDTO;

// imports
import java.time.LocalDateTime;
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;


public record UserDeletedDTO (

   UUID id,
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