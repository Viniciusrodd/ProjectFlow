
// packages
package com.example.ProjectFlow.modules.user.repository;

// mongo imports
import org.springframework.data.mongodb.repository.MongoRepository;

// import entities
import com.example.ProjectFlow.modules.user.document.ProfileImagesDocument;


public interface ProfileImageRepository extends MongoRepository<ProfileImagesDocument, String> {
 
   // find image by user id
   ProfileImagesDocument findByUserId(Long userId);

   // delete by user id
   void deleteByUserId(Long userId);

   // exists by user id
   boolean existsByUserId(Long userId);

}