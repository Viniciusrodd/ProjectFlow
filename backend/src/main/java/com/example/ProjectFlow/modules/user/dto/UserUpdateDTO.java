
// packages
package com.example.ProjectFlow.modules.user.dto;


public record UserUpdateDTO (

   String name,
   String email,
   String password

) {

   public UserUpdateDTO withEncryptedPassword(String encryptedPassword) {
      return new UserUpdateDTO(
         name, 
         email, 
         encryptedPassword
      );
   }

}