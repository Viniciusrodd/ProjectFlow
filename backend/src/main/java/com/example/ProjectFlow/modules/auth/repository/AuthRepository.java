
// packages
package com.example.ProjectFlow.modules.auth.repository;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.auth.dto.RegisterDTO;
import com.example.ProjectFlow.modules.auth.dto.RegisterResponseDTO;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;


@Repository
public class AuthRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;
   
   // register
   public RegisterResponseDTO register(RegisterDTO data) {
      UserEntity userEntity = new UserEntity();
        
      userEntity.setName(data.name());
      userEntity.setEmail(data.email());
      userEntity.setPassword(data.password());
      
      this.entityManager.persist(userEntity);
      
      return new RegisterResponseDTO(
         userEntity.getId(),
         userEntity.getName(),
         userEntity.getEmail(),
         userEntity.getCreatedAt()
      );
   }

}