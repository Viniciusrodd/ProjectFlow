
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

}