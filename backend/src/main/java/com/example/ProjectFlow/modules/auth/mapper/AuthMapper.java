
// packages
package com.example.ProjectFlow.modules.auth.mapper;

// imports
import org.springframework.stereotype.Component;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;

// import DTOs
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterResponseDTO;
import com.example.ProjectFlow.modules.user.dto.userDTO.UserDTO;
import com.example.ProjectFlow.modules.auth.dto.loginDTO.LoginResponseDTO;


@Component 
public class AuthMapper {

   // from UserEntity to RegisterResponseDTO
   public RegisterResponseDTO toRegisterResponseDTO(UserEntity entity) {
      return new RegisterResponseDTO(
         entity.getId(),
         entity.getName(),
         entity.getEmail(),
         entity.getCreatedAt()
      );
   }


   // from UserDTO to LoginResponseDTO
   public LoginResponseDTO toLoginResponseDTO(UserDTO dto, String token) {
      return new LoginResponseDTO(
         dto.id(),
         dto.name(),
         dto.email(),
         token
      );
   }

}