
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

}