
// packages
package com.example.ProjectFlow.modules.user.dto;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;


public record UserDTO (
   
   Long id,
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