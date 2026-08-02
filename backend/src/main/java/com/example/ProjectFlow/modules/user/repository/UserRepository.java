
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


   // exists by id
   public boolean existsById(Long id) {
      Long count = entityManager
         .createQuery("SELECT COUNT(u) FROM UserEntity u WHERE u.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
   }

   
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


   // exists by email
   public boolean existsByEmail(String email) {
      Long count = entityManager
         .createQuery("SELECT COUNT(u) FROM UserEntity u WHERE u.email = :email", Long.class)
         .setParameter("email", email)
         .getSingleResult();
      
      return count > 0;
   }


   // update profile image id
   public UserEntity updateProfileImageId(Long userId, String profileImageId) {
      UserEntity user = entityManager.find(UserEntity.class, userId);

      // update
      user.setProfileImageId(profileImageId);
      this.entityManager.merge(user);

      return user;
   }

}