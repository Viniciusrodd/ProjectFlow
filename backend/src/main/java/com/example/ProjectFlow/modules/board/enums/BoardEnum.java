
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

   // is valid
   public static boolean isValid(String type) {
      if(type == null) return false;
      
      for(BoardEnum board : BoardEnum.values()) {
         if(board.getType().equalsIgnoreCase(type)) {
            return true;
         }
      }
      
      return false;
   }

}