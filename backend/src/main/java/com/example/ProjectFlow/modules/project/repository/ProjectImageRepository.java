
// packages
package com.example.ProjectFlow.modules.project.repository;

// imports
import java.util.UUID;

// mongo imports
import org.springframework.data.mongodb.repository.MongoRepository;

// import document
import com.example.ProjectFlow.modules.project.document.ProjectImageDocument;


public interface ProjectImageRepository extends MongoRepository<ProjectImageDocument, String> {
 
   // find image by project id
   ProjectImageDocument findByProjectId(UUID projectId);

   // delete image by project id
   void deleteByProjectId(UUID projectId);

   // exists by project id   
   boolean existsByProjectId(UUID projectId);

}