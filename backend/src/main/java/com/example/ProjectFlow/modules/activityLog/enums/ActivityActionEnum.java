
// packages
package com.example.ProjectFlow.modules.activityLog.enums;


public enum ActivityActionEnum {
   
   // organization
   ORGANIZATION_CREATED("organization_created"),
   ORGANIZATION_UPDATED("organization_updated"),
   ORGANIZATION_DELETED("organization_deleted"),
   ORGANIZATION_LOGO_UPDATED("organization_logo_updated"),
   ORGANIZATION_LOGO_REMOVED("organization_logo_removed"),
   ORGANIZATION_MEMBER_ADDED("organization_member_added"),
   ORGANIZATION_MEMBER_REMOVED("organization_member_removed"),
   ORGANIZATION_MEMBER_ROLE_UPDATED("organization_member_role_updated"),

   // project
   PROJECT_CREATED("project_created"),
   PROJECT_UPDATED("project_updated"),
   PROJECT_DELETED("project_deleted"),
   PROJECT_LOGO_UPDATED("project_logo_updated"),
   PROJECT_LOGO_REMOVED("project_logo_removed"),
   PROJECT_MEMBER_ADDED("project_member_added"),
   PROJECT_MEMBER_REMOVED("project_member_removed"),

   // task
   TASK_CREATED("task_created"),
   TASK_UPDATED("task_updated"),
   TASK_DELETED("task_deleted"),
   
   // task label
   TASK_LABEL_ADDED("label_added"),
   TASK_LABEL_REMOVED("label_removed"),
   
   // task checklist
   TASK_CHECKLIST_CREATED("task_checklist_created"),
   TASK_CHECKLIST_UPDATED("task_checklist_updated"),
   TASK_POSITION_UPDATED("task_position_updated"),
   TASK_COMPLETED("task_completed"),
   
   // comment
   COMMENT_CREATED("comment_created"),
   COMMENT_UPDATED("comment_updated"),
   COMMENT_DELETED("comment_deleted"),

   // attachment
   ATTACHMENT_UPLOADED("attachment_uploaded"),
   ATTACHMENT_DELETED("attachment_deleted");


   private final String type;


   // constructor
   private ActivityActionEnum(String type) {
      this.type = type;
   }


   // getters
   public String getType() { return type; }


   // is valid
   public static boolean isValid(String type) {
      if(type == null) return false;
      
      for(ActivityActionEnum action : ActivityActionEnum.values()) {
         if(action.getType().equalsIgnoreCase(type)) {
            return true;
         }
      }
      
      return false;
   }

}