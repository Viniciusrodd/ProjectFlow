
// packages
package com.example.ProjectFlow.modules.auth.repository;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterDTO;
import com.example.ProjectFlow.modules.auth.dto.registerDTO.RegisterResponseDTO;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;


@Repository
public class AuthRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;
   
   // register
   @Transactional
   public UserEntity register(RegisterDTO data) {
      UserEntity user = new UserEntity();
        
      user.setName(data.name());
      user.setEmail(data.email());
      user.setPassword(data.password());
      
      this.entityManager.persist(user);
      
      return user;
   }

}