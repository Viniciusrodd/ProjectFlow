
// packages
package com.example.ProjectFlow.modules.notification.dto;

// import enums
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;


public record NotificationDTO (

   String userId,
   NotificationTitleEnum title,
   String message

) {

   // builder
   public static class Builder {
      private String userId;
      private NotificationTitleEnum title;
      private String message;

      public Builder userId(String userId) {
         this.userId = userId;
         return this;
      }

      public Builder title(NotificationTitleEnum title) {
         this.title = title;
         return this;
      }

      public Builder message(String message) {
         this.message = message;
         return this;
      }

      public NotificationDTO build() {
         return new NotificationDTO(userId, title, message);
      }
   }

}