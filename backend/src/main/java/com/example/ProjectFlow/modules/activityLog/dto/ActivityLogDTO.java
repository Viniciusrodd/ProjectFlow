
// packages
package com.example.ProjectFlow.modules.activityLog.dto;

// imports
import java.util.UUID;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


public record ActivityLogDTO (

   UUID organizationId,
   UUID projectId,
   UUID taskId,
   UUID commentId,
   UUID attachmentId,
   UUID userId,
   ActivityActionEnum action,
   String description

) {

   // builder
   public static class Builder {
      private UUID organizationId;
      private UUID projectId;
      private UUID taskId;
      private UUID commentId;
      private UUID attachmentId;
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

      public Builder commentId(UUID commentId) {
         this.commentId = commentId;
         return this;
      }

      public Builder attachmentId(UUID attachmentId) {
         this.attachmentId = attachmentId;
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

      public ActivityLogDTO build() {
         return new ActivityLogDTO(organizationId, projectId, taskId, commentId, attachmentId, userId, action, description);
      }
   }

}