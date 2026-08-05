
// packages
package com.example.ProjectFlow.modules.user.repository;

// imports
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserDTO;
import com.example.ProjectFlow.modules.user.dto.UserProfileDTO;

// import entity
import com.example.ProjectFlow.modules.user.entity.UserEntity;


@Repository
public class UserRepository {
 
   // properties
   @PersistenceContext
   private EntityManager entityManager;


   // get all
   public List<UserProfileDTO> getAllUsers() {
      List<UserEntity> usersDocument = entityManager
         .createQuery("SELECT u FROM UserEntity u ORDER BY u.id ASC", UserEntity.class)
         .getResultList();
      
      List<UserProfileDTO> users = new ArrayList<>();
      
      for(UserEntity user : usersDocument) {
         users.add(UserProfileDTO.get(user));
      }

      return users;
   }


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
      
      return UserDTO.get(user);
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

      return user;
   }


   // remove profile image id
   public UserEntity removeProfileImageId(Long userId) {
      UserEntity user = entityManager.find(UserEntity.class, userId);

      // remove
      user.setProfileImageId(null);

      return user;
   }

}