
// packages
package com.example.ProjectFlow.modules.user.dto;

// import eneities
import com.example.ProjectFlow.modules.user.entity.UserEntity;


public record UserProfileDTO (

   Long id,
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