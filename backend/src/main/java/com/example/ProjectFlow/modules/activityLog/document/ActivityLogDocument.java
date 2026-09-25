
// packages
package com.example.ProjectFlow.modules.activityLog.document;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;

// mongodb imports
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


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
   private ActivityActionEnum action;

   @Field("description")
   private String description;

   @Field("createdAt")
   private LocalDateTime createdAt;


   // constructor - empty
   public ActivityLogDocument() {}


   // constructor - builder
   public ActivityLogDocument(Builder builder) {
      setOrganizationId(builder.organizationId);
      setProjectId(builder.projectId);
      setTaskId(builder.taskId);
      setUserId(builder.userId);
      setAction(builder.action);
      setDescription(builder.description);
   }

   
   // getters
   public String getId() { return this.id; }
   public UUID getOrganizationId() { return this.organizationId; }
   public UUID getProjectId() { return this.projectId; }
   public UUID getTaskId() { return this.taskId; }
   public UUID getUserId() { return this.userId; }
   public ActivityActionEnum getAction() { return this.action; }
   public String getDescription() { return this.description; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }


   // setters
   public void setId(String id) { this.id = id; }
   public void setOrganizationId(UUID organizationId) { this.organizationId = organizationId; }
   public void setProjectId(UUID projectId) { this.projectId = projectId; }
   public void setTaskId(UUID taskId) { this.taskId = taskId; }
   public void setUserId(UUID userId) { this.userId = userId; }
   public void setAction(ActivityActionEnum action) { this.action = action; }
   public void setDescription(String description) { this.description = description; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }


   //// builder


   public static class Builder {
      private UUID organizationId;
      private UUID projectId;
      private UUID taskId;
      private UUID userId;
      private ActivityActionEnum action;
      private String description;

      public Builder organizationId(UUID organizationId) {
         this.organizationId = organizationId;
         return this;
      }

      public Builder projectId(UUID projectId) {
         this.projectId = projectId;
         return this;
      }

      public Builder taskId(UUID taskId) {
         this.taskId = taskId;
         return this;
      }

      public Builder userId(UUID userId) {
         this.userId = userId;
         return this;
      }

      public Builder action(ActivityActionEnum action) {
         this.action = action;
         return this;
      }

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public ActivityLogDocument build() {
         return new ActivityLogDocument(this);
      }
   }

}