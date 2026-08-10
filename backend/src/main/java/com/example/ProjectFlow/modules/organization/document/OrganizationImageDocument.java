
// packages
package com.example.ProjectFlow.modules.organization.document;

// imports
import java.time.LocalDateTime;
import java.util.UUID;

// mongodb imports
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "organization_images")
public class OrganizationImageDocument {
   
   @Id
   private String id;

   @Field("organizationId")
   private UUID organizationId; // mysql ref.

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
   public OrganizationImageDocument() {}


   // constructor - builder
   public OrganizationImageDocument(Builder builder) {
      setOrganizationId(builder.organizationId);
      setFileName(builder.fileName);
      setMimeType(builder.mimeType);
      setSize(builder.size);
      setUploadDate(builder.uploadDate);
      setBinary(builder.binary);
   }


   // getters
   public String getId() { return this.id; }
   public UUID getOrganizationId() { return this.organizationId; }
   public String getFileName() { return this.fileName; }
   public String getMimeType() { return this.mimeType; }
   public Long getSize() { return this.size; }
   public LocalDateTime getUploadDate() { return this.uploadDate; }
   public byte[] getBinary() { return this.binary; }


   // setters
   public void setId(String id) { this.id = id; }
   public void setOrganizationId(UUID organizationId) { this.organizationId = organizationId; }
   public void setFileName(String fileName) { this.fileName = fileName; }
   public void setMimeType(String mimeType) { this.mimeType = mimeType; }
   public void setSize(Long size) { this.size = size; }
   public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
   public void setBinary(byte[] binary) { this.binary = binary; }


   //// builder


   public static class Builder {
      private UUID organizationId;
      private String fileName;
      private String mimeType;
      private Long size;
      private LocalDateTime uploadDate;
      private byte[] binary;

      public Builder organizationId(UUID organizationId) {
         this.organizationId = organizationId;
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

      public OrganizationImageDocument build() {
         return new OrganizationImageDocument(this);
      }
   }

}