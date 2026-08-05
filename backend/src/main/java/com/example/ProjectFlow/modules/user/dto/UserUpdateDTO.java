
// packages
package com.example.ProjectFlow.modules.user.dto;

// imports
import java.util.Optional;


public record UserUpdateDTO (

   Optional<String> name,
   Optional<String> email,
   Optional<String> password

) {

   public UserUpdateDTO withEncryptedPassword(String encryptedPassword) {
      return new UserUpdateDTO(
         name, 
         email, 
         Optional.of(encryptedPassword)
      );
   }

}