
// packages
package com.example.ProjectFlow.modules.audit.enums;


public enum OperationEnum {
 
   INSERT("insert"),
   UPDATE("update"),
   DELETE("delete");

   private final String type;

   // constructor
   private OperationEnum(String type) {
      this.type = type;
   }

   // getters
   public String getType() { return type; }

}