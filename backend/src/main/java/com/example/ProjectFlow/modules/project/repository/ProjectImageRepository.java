
// packages
package com.example.ProjectFlow.modules.project.repository;

// imports
import java.util.UUID;

// mongo imports
import org.springframework.data.mongodb.repository.MongoRepository;

// import document
import com.example.ProjectFlow.modules.project.document.ProjectImagesDocument;


public interface ProjectImageRepository extends MongoRepository<ProjectImagesDocument, String> {
 
   // find image by project id
   ProjectImagesDocument findByProjectId(UUID projectId);

   // delete image by project id
   void deleteByProjectId(UUID projectId);

   // exists by project id   
   boolean existsByProjectId(UUID projectId);

}