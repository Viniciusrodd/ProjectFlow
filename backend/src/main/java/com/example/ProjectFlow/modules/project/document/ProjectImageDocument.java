
// packages
package com.example.ProjectFlow.modules.project.document;

// imports
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;

// mongodb imports
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "project_images")
public class ProjectImageDocument {
 
   @Id
   private String id;

   @Field("projectId")
   private UUID projectId; // mysql ref.

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


   // constructor - empty
   public ProjectImageDocument() {}


   // constructor - builder
   public ProjectImageDocument(Builder builder) {
      setProjectId(builder.projectId);
      setFileName(builder.fileName);
      setMimeType(builder.mimeType);
      setSize(builder.size);
      setUploadDate(builder.uploadDate);
      setBinary(builder.binary);
   }


   // getters
   public String getId() { return this.id; }
   public UUID getProjectId() { return this.projectId; }
   public String getFileName() { return this.fileName; }
   public String getMimeType() { return this.mimeType; }
   public Long getSize() { return this.size; }
   public LocalDateTime getUploadDate() { return this.uploadDate; }
   public byte[] getBinary() { return this.binary; }


   // setters
   public void setId(String id) { this.id = id; }
   public void setProjectId(UUID projectId) { this.projectId = projectId; }
   public void setFileName(String fileName) { this.fileName = fileName; }
   public void setMimeType(String mimeType) { this.mimeType = mimeType; }
   public void setSize(Long size) { this.size = size; }
   public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
   public void setBinary(byte[] binary) { this.binary = binary; }


   //// builder


   public static class Builder {
      private UUID projectId;
      private String fileName;
      private String mimeType;
      private Long size;
      private LocalDateTime uploadDate;
      private byte[] binary;

      public Builder projectId(UUID projectId) {
         this.projectId = projectId;
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

      public ProjectImageDocument build() {
         return new ProjectImageDocument(this);
      }
   }

}