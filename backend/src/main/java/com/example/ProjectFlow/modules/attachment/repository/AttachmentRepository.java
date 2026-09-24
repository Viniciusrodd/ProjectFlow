
// packages
package com.example.ProjectFlow.modules.attachment.repository;

// imports
import java.util.UUID;

// mongo imports
import org.springframework.data.mongodb.repository.MongoRepository;

// import document
import com.example.ProjectFlow.modules.attachment.document.AttachmentDocument;


public interface AttachmentRepository extends MongoRepository<AttachmentDocument, String> {
 
   // find attachment by task id
   AttachmentDocument findByTaskId(UUID taskId);

   // delete attachment by task id
   void deleteByTaskId(UUID taskId);

   // exists by task id   
   boolean existsByTaskId(UUID taskId);

}