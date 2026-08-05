
// packages
package com.example.ProjectFlow.modules.activityLog.document;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;

// mongodb imports
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "activity_logs")
public class ActivityLogDocument {
 
   @Id
   private String id;

   @Field("organizationId")
   private UUID organizationId; // mysql ref.

   @Field("projectId")
   private UUID projectId; // mysql ref.

   @Field("taskId")
   private UUID taskId; // mysql ref.

   @Field("userId")
   private UUID userId; // mysql ref.

   @Field("action")
   private String action;

   @Field("description")
   private String description;

   @Field("createdAt")
   private LocalDateTime createdAt;


   // constructor
   protected ActivityLogDocument() {}

   
   // getters
   public String getId() { return this.id; }
   public UUID getOrganizationId() { return this.organizationId; }
   public UUID getProjectId() { return this.projectId; }
   public UUID getTaskId() { return this.taskId; }
   public UUID getUserId() { return this.userId; }
   public String getAction() { return this.action; }
   public String getDescription() { return this.description; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }


   // setters
   public void setId(String id) { this.id = id; }
   public void setOrganizationId(UUID organizationId) { this.organizationId = organizationId; }
   public void setProjectId(UUID projectId) { this.projectId = projectId; }
   public void setTaskId(UUID taskId) { this.taskId = taskId; }
   public void setUserId(UUID userId) { this.userId = userId; }
   public void setAction(String action) { this.action = action; }
   public void setDescription(String description) { this.description = description; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}