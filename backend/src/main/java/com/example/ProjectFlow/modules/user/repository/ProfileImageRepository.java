
// packages
package com.example.ProjectFlow.modules.user.repository;

// imports
import java.util.UUID;

// mongo imports
import org.springframework.data.mongodb.repository.MongoRepository;

// import entities
import com.example.ProjectFlow.modules.user.document.ProfileImageDocument;


public interface ProfileImageRepository extends MongoRepository<ProfileImageDocument, String> {
 
   // find image by user id
   ProfileImageDocument findByUserId(UUID userId);

   // delete by user id
   void deleteByUserId(UUID userId);

   // exists by user id
   boolean existsByUserId(UUID userId);

}