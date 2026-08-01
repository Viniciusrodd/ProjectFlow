
// packages
package com.example.ProjectFlow.modules.user.repository;

// imports
import org.springframework.stereotype.Repository;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserDTO;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;


@Repository
public class UserRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;

   // find by email
   public UserDTO findByEmail(String email) {
      UserEntity user = entityManager
         .createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class)
         .setParameter("email", email)
         .getSingleResult();
      
      return new UserDTO(
         user.getId(),
         user.getEmail(),
         user.getName(),
         user.getPassword()
      );
   }

}