
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

}