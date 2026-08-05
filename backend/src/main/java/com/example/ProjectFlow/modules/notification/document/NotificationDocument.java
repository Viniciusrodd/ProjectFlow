
// packages
package com.example.ProjectFlow.modules.notification.document;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;

// mongodb imports
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "notifications")
public class NotificationDocument {
 
   @Id
   private String id;

   @Field("userId")
   private UUID userId; // mysql ref.

   @Field("title")
   private String title;

   @Field("message")
   private String message;

   @Field("read")
   private boolean read;

   @Field("createdAt")
   private LocalDateTime createdAt;


   // constructor
   protected NotificationDocument() {}


   // getters
   public String getId() { return this.id; }
   public UUID getUserId() { return this.userId; }
   public String getTitle() { return this.title; }
   public String getMessage() { return this.message; }
   public boolean getRead() { return this.read; }
   public LocalDateTime getCreatedAt() { return this.createdAt; }

   // setters
   public void setId(String id) { this.id = id; }
   public void setUserId(UUID userId) { this.userId = userId; }
   public void setTitleId(String title) { this.title = title; }
   public void setMessageId(String message) { this.message = message; }
   public void setRead(boolean read) { this.read = read; }
   public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}