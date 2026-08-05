
// packages
package com.example.ProjectFlow.modules.user.dto;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;


public record UserDTO (
   
   UUID id,
   String email,
   String name,
   String password

) {

   public static UserDTO get(UserEntity document) {
      return new UserDTO(
         document.getId(),
         document.getEmail(),
         document.getName(),
         document.getPassword()
      );
   }

}