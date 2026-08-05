
// packages
package com.example.ProjectFlow.modules.user.dto;

// imports
import java.util.UUID;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;


public record UserProfileDTO (

   UUID id,
   String email,
   String name,
   String profileImageId

) {

   public static UserProfileDTO get(UserEntity document) {
      return new UserProfileDTO(
         document.getId(),
         document.getEmail(),
         document.getName(),
         document.getProfileImageId()
      );
   }

}