
// packages
package com.example.ProjectFlow.modules.organization.enums;


public enum RoleEnum {
 
   OWNER("owner"),
   ADMIN("admin"),
   MEMBER("member");

   private final String type;

   // constructor
   private RoleEnum(String type) {
      this.type = type;
   }

   // getters
   public String getType() { return type; }

   // is valid
   public static boolean isValid(String type) {
      if(type == null) return false;
      
      for(RoleEnum role : RoleEnum.values()) {
         if(role.getType().equalsIgnoreCase(type)) {
            return true;
         }
      }
      
      return false;
   }

}