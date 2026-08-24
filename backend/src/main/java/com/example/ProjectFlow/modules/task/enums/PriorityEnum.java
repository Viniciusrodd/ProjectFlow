
// packages
package com.example.ProjectFlow.modules.task.enums;


public enum PriorityEnum {
 
   LOW("low"),
   MEDIUM("medium"),
   HIGH("high");

   private final String type;

   // constructor
   private PriorityEnum(String type) {
      this.type = type;
   }

   // getters
   public String getType() { return type; }

   // is valid
   public static boolean isValid(String type) {
      if(type == null) return false;

      for(PriorityEnum priority : PriorityEnum.values()) {
         if(priority.getType().equalsIgnoreCase(type)) {
            return true;
         }
      }

      return false;
   }

}