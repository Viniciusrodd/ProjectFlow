
// packages
package com.example.ProjectFlow.modules.project.enums;


public enum StatusEnum {
 
   ACTIVE("active"),
   FINISH("finish");

   private final String type;

   // constructor
   private StatusEnum(String type) {
      this.type = type;
   }

   // getters
   public String getType() { return type; }

   // is valid
   public static boolean isValid(String type) {
      if(type == null) return false;

      for(StatusEnum status : StatusEnum.values()) {
         if(status.getType().equalsIgnoreCase(type)) {
            return true;
         }
      }

      return false;
   }

}