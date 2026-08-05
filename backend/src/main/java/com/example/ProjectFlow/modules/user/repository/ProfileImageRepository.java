
// packages
package com.example.ProjectFlow.modules.user.repository;

// mongo imports
import org.springframework.data.mongodb.repository.MongoRepository;

// import entities
import com.example.ProjectFlow.modules.user.document.ProfileImageDocument;


public interface ProfileImageRepository extends MongoRepository<ProfileImageDocument, String> {
 
   // find image by user id
   ProfileImageDocument findByUserId(Long userId);

   // delete by user id
   void deleteByUserId(Long userId);

   // exists by user id
   boolean existsByUserId(Long userId);

}