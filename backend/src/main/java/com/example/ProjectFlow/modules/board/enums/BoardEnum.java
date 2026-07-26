
// packages
package com.example.ProjectFlow.modules.board.enums;


public enum BoardEnum {
 
   TODO("to do"),
   PROGRESS("progress"),
   DONE("done"),
   APPROVED("approved");

   private final String type;

   // constructor
   private BoardEnum(String type) {
      this.type = type;
   }

   // getters
   public String getType() { return type; }

}