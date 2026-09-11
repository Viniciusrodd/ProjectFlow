
// packages
package com.example.ProjectFlow.modules.user.mapper;

// imports
import org.springframework.stereotype.Component;

// import entities
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.userDTO.UserDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserDeletedDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserProfileDTO;


@Component 
public class UserMapper {
 
   // from UserEntity to UserDTO
   public UserDTO toUserDTO(UserEntity entity) {
      return new UserDTO(
         entity.getId(),
         entity.getEmail(),
         entity.getName(),
         entity.getPassword()
      );
   }


   // from UserEntity to UserProfileDTO
   public UserProfileDTO toUserProfileDTO(UserEntity entity) {
      return new UserProfileDTO(
         entity.getId(),
         entity.getEmail(),
         entity.getName(),
         entity.getProfileImageId()
      );
   }


   // from UserEntity to UserDeletedDTO
   public UserDeletedDTO toUserDeletedDTO(UserEntity entity) {
      return new UserDeletedDTO(
         entity.getId(),
         entity.getName(),
         entity.getEmail(),
         entity.getDeletedAt()
      );
   }

}