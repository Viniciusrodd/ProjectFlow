
// packages
package com.example.ProjectFlow.modules.notification.document;

// imports
import java.sql.Date;
import org.springframework.data.annotation.Id;

// mongodb imports
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "notifications")
public class NotificationDocument {
 
   @Id
   private String id;

   @Field("userId")
   private Long userId; // mysql ref.

   @Field("title")
   private String title;

   @Field("message")
   private String message;

   @Field("read")
   private boolean read;

   @Field("createdAt")
   private Date createdAt;


   // constructor
   protected NotificationDocument() {}


   // getters
   public String getId() { return this.id; }
   public Long getUserId() { return this.userId; }
   public String getTitle() { return this.title; }
   public String getMessage() { return this.message; }
   public boolean getRead() { return this.read; }
   public Date getCreatedAt() { return this.createdAt; }

   // setters
   public void setId(String id) { this.id = id; }
   public void setUserId(Long userId) { this.userId = userId; }
   public void setTitleId(String title) { this.title = title; }
   public void setMessageId(String message) { this.message = message; }
   public void setRead(boolean read) { this.read = read; }
   public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

}