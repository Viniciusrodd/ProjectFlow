
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
   ActivityLogDocument existsByOrganizationId(UUID organizationId);

   // exists by project id
   ActivityLogDocument existsByProjectId(UUID projectId);

   // exists by task id
   ActivityLogDocument existsByTaskId(UUID taskId);

   // exists by user id
   ActivityLogDocument existsByUserId(UUID userId);

   ////

   // delete all by organization id
   ActivityLogDocument deleteAllByOrganizationId(UUID organizationId);

   // delete all by project id
   ActivityLogDocument deleteAllByProjectId(UUID projectId);

   // delete all by task id
   ActivityLogDocument deleteAllByTaskId(UUID taskId);

   // delete all by user id
   ActivityLogDocument deleteAllByUserId(UUID userId);

}