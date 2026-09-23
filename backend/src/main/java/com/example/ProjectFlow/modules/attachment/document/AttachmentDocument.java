
// packages
package com.example.ProjectFlow.modules.attachment.document;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;

// mongodb imports
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "attachments")
public class AttachmentDocument {
 
   @Id
   private String id;

   @Field("taskId")
   private UUID taskId; // mysql ref.

   @Field("uploadedBy")
   private UUID uploadedBy; // mysql ref. - user

   @Field("fileName")
   private String fileName;

   @Field("mimeType")
   private String mimeType;

   @Field("size")
   private Long size;

   @Field("uploadDate")
   private LocalDateTime uploadDate;
   
   @Field("binary")
   private byte[] binary; // image data


   // constructor
   public AttachmentDocument() {}


   // constructor - builder
   public AttachmentDocument(Builder builder) {
      setTaskId(builder.taskId);
      setUploadedBy(builder.uploadedBy);
      setFileName(builder.fileName);
      setMimeType(builder.mimeType);
      setSize(builder.size);
      setUploadDate(builder.uploadDate);
      setBinary(builder.binary);
   }

   // getters
   public String getId() { return this.id; }
   public UUID getTaskId() { return this.taskId; }
   public UUID getUploadedBy() { return this.uploadedBy; }
   public String getFileName() { return this.fileName; }
   public String getMimeType() { return this.mimeType; }
   public Long getSize() { return this.size; }
   public LocalDateTime getUploadDate() { return this.uploadDate; }
   public byte[] getBinary() { return this.binary; }


   // setters
   public void setId(String id) { this.id = id; }
   public void setTaskId(UUID taskId) { this.taskId = taskId; }
   public void setUploadedBy(UUID uploadedBy) { this.uploadedBy = uploadedBy; }
   public void setFileName(String fileName) { this.fileName = fileName; }
   public void setMimeType(String mimeType) { this.mimeType = mimeType; }
   public void setSize(Long size) { this.size = size; }
   public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
   public void setBinary(byte[] binary) { this.binary = binary; }   


   //// builder


   public static class Builder {
      private UUID taskId;
      private UUID uploadedBy;
      private String fileName;
      private String mimeType;
      private Long size;
      private LocalDateTime uploadDate;
      private byte[] binary;

      public Builder taskId(UUID taskId) {
         this.taskId = taskId;
         return this;
      }

      public Builder uploadedBy(UUID uploadedBy) {
         this.uploadedBy = uploadedBy;
         return this;
      }

      public Builder fileName(String fileName) {
         this.fileName = fileName;
         return this;
      }

      public Builder mimeType(String mimeType) {
         this.mimeType = mimeType;
         return this;
      }

      public Builder size(Long size) {
         this.size = size;
         return this;
      }

      public Builder uploadDate(LocalDateTime uploadDate) {
         this.uploadDate = uploadDate;
         return this;
      }

      public Builder binary(byte[] binary) {
         this.binary = binary;
         return this;
      }

      public AttachmentDocument build() {
         return new AttachmentDocument(this);
      }
   }

}