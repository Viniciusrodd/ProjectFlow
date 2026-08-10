
// packages
package com.example.ProjectFlow.common.constants;


public class ValidationConstants {
 
   // max lengths
   public static final int MAX_NAME_LENGTH = 120;
   public static final int MAX_EMAIL_LENGTH = 255;
   public static final int MAX_PASSWORD_LENGTH = 255;
   public static final int MAX_TITLE_LENGTH = 150;
   public static final int MAX_DESCRIPTION_LENGTH = 4000;
   public static final int MAX_COMMENT_LENGTH = 3000;
   public static final int MAX_LABEL_NAME_LENGTH = 60;
   public static final int MAX_CHECKLIST_DESC_LENGTH = 255;
   public static final int MAX_ORGANIZATION_NAME_LENGTH = 120;
   public static final int MAX_PROJECT_NAME_LENGTH = 120;

   // min lengths
   public static final int MIN_NAME_LENGTH = 4;
   public static final int MIN_PASSWORD_LENGTH = 8;
   public static final int MIN_TITLE_LENGTH = 3;
   public static final int MIN_ORGANIZATION_NAME_LENGTH = 4;
   public static final int MIN_PROJECT_NAME_LENGTH = 4;

   // file limits
   public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
   public static final long MAX_ORGANIZATION_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
   public static final long MAX_PROJECT_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
   public static final long MAX_ATTACHMENT_SIZE = 25 * 1024 * 1024; // 25MB

   // image MIME types
   public static final String[] ALLOWED_IMAGE_TYPES = {
      "image/jpg", "image/jpeg", "image/png", "image/webp"
   };

   // attachment MIME types
   public static final String[] ALLOWED_ATTACHMENT_TYPES = {
      "application/pdf", 
      "image/jpg", "image/jpeg", "image/png", "image/webp",
      "application/msword", 
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      "application/vnd.ms-excel",
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "text/plain"
   };

}