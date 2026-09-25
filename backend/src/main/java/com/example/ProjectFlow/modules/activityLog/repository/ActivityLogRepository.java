
// packages
package com.example.ProjectFlow.modules.activityLog.repository;

// imports
import java.util.UUID;

// mongo imports
import org.springframework.data.mongodb.repository.MongoRepository;

// import document
import com.example.ProjectFlow.modules.activityLog.document.ActivityLogDocument;


public interface ActivityLogRepository extends MongoRepository<ActivityLogDocument, String> {
 
   // find activity log by organization id
   ActivityLogDocument findByOrganizationId(UUID organizationId);

   // find activity log by project id
   ActivityLogDocument findByProjectId(UUID projectId);

   // find activity log by task id
   ActivityLogDocument findByTaskId(UUID taskId);

   // find activity log by user id
   ActivityLogDocument findByUserId(UUID userId);

   ////

   // exists by organization id
   Boolean existsByOrganizationId(UUID organizationId);

   // exists by project id
   Boolean existsByProjectId(UUID projectId);

   // exists by task id
   Boolean existsByTaskId(UUID taskId);

   // exists by user id
   Boolean existsByUserId(UUID userId);

   ////

   // delete all by organization id
   void deleteAllByOrganizationId(UUID organizationId);

   // delete all by project id
   void deleteAllByProjectId(UUID projectId);

   // delete all by task id
   void deleteAllByTaskId(UUID taskId);

   // delete all by user id
   void deleteAllByUserId(UUID userId);

}