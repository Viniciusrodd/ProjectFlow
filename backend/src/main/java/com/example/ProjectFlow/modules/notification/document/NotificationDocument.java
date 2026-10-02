
// packages
package com.example.ProjectFlow.modules.notification.document;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;

// mongodb imports
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// import enums
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;


@Document(collection = "notifications")
public class NotificationDocument {
 
   @Id
   private String id;

   @Field("userId")
   private UUID userId; // mysql ref.

   @Field("title")
   private NotificationTitleEnum title;

   @Field("message")
   private String message;

   @Field("read")
   private boolean read;

   @Field("createdAt")
   private LocalDateTime createdAt;


   // constructor - empty
   public NotificationDocument() {}


   // constructor - builder
   public NotificationDocument(Builder builder) {
      setUserId(builder.userId);
      setTitle(builder.title);
      setMessage(builder.message);
      setRead(builder.read);
      setCreatedAt(builder.createdAt);
   }


   // getters
   public String getId() { return this.id; }
   public UUID getUserId() { return this.userId; }
   public NotificationTitleEnum getTitle() { return this.title; }
   public String getMessage() { return this.message; }
   public boolean getRead() { return this.read; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }

   // setters
   public void setId(String id) { this.id = id; }
   public void setUserId(UUID userId) { this.userId = userId; }
   public void setTitle(NotificationTitleEnum title) { this.title = title; }
   public void setMessage(String message) { this.message = message; }
   public void setRead(boolean read) { this.read = read; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }


   // builder


   public static class Builder {
      private UUID userId;
      private NotificationTitleEnum title;
      private String message;
      private boolean read;
      private LocalDateTime createdAt;

      public Builder userId(UUID userId) {
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

      public Builder read(boolean read) {
         this.read = read;
         return this;
      }

      public Builder createdAt(LocalDateTime createdAt) {
         this.createdAt = createdAt;
         return this;
      }

      public NotificationDocument build() {
         return new NotificationDocument(this);
      }
   }

}