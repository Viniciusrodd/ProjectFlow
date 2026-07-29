
// packages
package com.example.ProjectFlow.modules.activityLog.document;

// imports
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

// mongodb imports
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "activity_logs")
public class ActivityLogDocument {
 
   @Id
   private String id;

   @Field("organizationId")
   private Long organizationId; // mysql ref.

   @Field("projectId")
   private Long projectId; // mysql ref.

   @Field("taskId")
   private Long taskId; // mysql ref.

   @Field("userId")
   private Long userId; // mysql ref.

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
   public Long getOrganizationId() { return this.organizationId; }
   public Long getProjectId() { return this.projectId; }
   public Long getTaskId() { return this.taskId; }
   public Long getUserId() { return this.userId; }
   public String getAction() { return this.action; }
   public String getDescription() { return this.description; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }


   // setters
   public void setId(String id) { this.id = id; }
   public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }
   public void setProjectId(Long projectId) { this.projectId = projectId; }
   public void setTaskId(Long taskId) { this.taskId = taskId; }
   public void setUserId(Long userId) { this.userId = userId; }
   public void setAction(String action) { this.action = action; }
   public void setDescription(String description) { this.description = description; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}