
// packages
package com.example.ProjectFlow.modules.user.repository;

// imports
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

// jakarta imports
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

// import DTOs
import com.example.ProjectFlow.modules.user.dto.UserDTO;
import com.example.ProjectFlow.modules.user.dto.UserProfileDTO;
import com.example.ProjectFlow.modules.user.dto.UserUpdateDTO;

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


   // get by id
   public UserProfileDTO getById(Long id) throws NoResultException {
      UserEntity user = entityManager
         .createQuery("SELECT u FROM UserEntity u WHERE u.id = :id", UserEntity.class)
         .setParameter("id", id)
         .getSingleResult();
      
      return UserProfileDTO.get(user);
   }

   
   // get by email
   public UserDTO getByEmail(String email) throws NoResultException {
      UserEntity user = entityManager
         .createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class)
         .setParameter("email", email)
         .getSingleResult();
      
      return UserDTO.get(user);
   }

   
   // exists by id
   public boolean existsById(Long id) {
      Long count = entityManager
         .createQuery("SELECT COUNT(u) FROM UserEntity u WHERE u.id = :id", Long.class)
         .setParameter("id", id)
         .getSingleResult();

      return count > 0;
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
   public UserEntity updateProfileImageId(Long userId, String profileImageId) throws NoResultException {
      UserEntity user = entityManager
         .createQuery("SELECT u FROM UserEntity u WHERE u.id = :userId", UserEntity.class)
         .setParameter("userId", userId)
         .getSingleResult();

      // update
      user.setProfileImageId(profileImageId);

      return user;
   }


   // remove profile image id
   public UserEntity removeProfileImageId(Long userId) throws NoResultException {
      UserEntity user = entityManager
         .createQuery("SELECT u FROM UserEntity u WHERE u.id = :userId", UserEntity.class)
         .setParameter("userId", userId)
         .getSingleResult();

      // remove
      user.setProfileImageId(null);

      return user;
   }


   // update user
   public void updateUser(Long userId, UserUpdateDTO data) throws NoResultException {
      UserEntity user = entityManager
         .createQuery("SELECT u FROM UserEntity u WHERE u.id = :userId", UserEntity.class)
         .setParameter("userId", userId)
         .getSingleResult();

      data.name().ifPresent(user::setName);
      data.email().ifPresent(user::setEmail);
      data.password().ifPresent(user::setPassword);
   }

}