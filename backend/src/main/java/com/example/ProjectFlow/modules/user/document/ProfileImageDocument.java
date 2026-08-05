
// packages
package com.example.ProjectFlow.modules.user.document;

// imports
import java.time.LocalDateTime;

// mongodb imports
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "profile_images")
public class ProfileImageDocument {

   @Id
   private String id;

   @Field("userId")
   private Long userId; // mysql ref.

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
   public ProfileImageDocument() {}


   // constructor
   public ProfileImageDocument(Builder builder) {
      setUserId(builder.userId);
      setFileName(builder.fileName);
      setMimeType(builder.mimeType);
      setSize(builder.size);
      setUploadDate(builder.uploadDate);
      setBinary(builder.binary);
   }


   // getters
   public String getId() { return this.id; }
   public Long getUserId() { return this.userId; }
   public String getFileName() { return this.fileName; }
   public String getMimeType() { return this.mimeType; }
   public Long getSize() { return this.size; }
   public LocalDateTime getUploadDate() { return this.uploadDate; }
   public byte[] getBinary() { return this.binary; }


   // setters
   public void setId(String id) { this.id = id; }
   public void setUserId(Long userId) { this.userId = userId; }
   public void setFileName(String fileName) { this.fileName = fileName; }
   public void setMimeType(String mimeType) { this.mimeType = mimeType; }
   public void setSize(Long size) { this.size = size; }
   public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
   public void setBinary(byte[] binary) { this.binary = binary; }


   //// builder


   public static class Builder {
      private Long userId;
      private String fileName;
      private String mimeType;
      private Long size;
      private LocalDateTime uploadDate;
      private byte[] binary;

      public Builder userId(Long userId) {
         this.userId = userId;
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

      public ProfileImageDocument build() {
         return new ProfileImageDocument(this);
      }
   }

}