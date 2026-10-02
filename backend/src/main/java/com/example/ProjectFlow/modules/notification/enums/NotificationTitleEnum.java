
// packages
package com.example.ProjectFlow.modules.notification.enums;

// imports
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum NotificationTitleEnum {
 
   // organization
   ORGANIZATION_MEMBER_ADDED("membro_adicionado_a_organizacao"),
   ORGANIZATION_MEMBER_ROLE_UPDATED("permissao_do_membro_da_organizacao_atualizada"),
   ORGANIZATION_MEMBER_REMOVED("membro_removido_da_organizacao"),


   // project
   PROJECT_MEMBER_ADDED("membro_adicionado_ao_projeto"),
   PROJECT_MEMBER_ROLE_UPDATED("permissao_do_membro_do_projeto_atualizada"),
   PROJECT_MEMBER_REMOVED("membro_removido_do_projeto"),


   // task
   TASK_ASSIGNED("tarefa_atribuida"),
   TASK_STATUS_UPDATED("status_da_tarefa_atualizado"),
   TASK_COMPLETED("tarefa_concluida"),
   TASK_DELETED("tarefa_excluida"),


   // task label
   TASK_LABEL_ADDED("etiqueta_da_tarefa_adicionada"),
   TASK_LABEL_REMOVED("etiqueta_da_tarefa_removida"),


   // task checklist
   TASK_CHECKLIST_UPDATED("checklist_da_tarefa_atualizado"),


   // comment
   TASK_COMMENT_CREATED("comentario_adicionado_a_tarefa"),


   // attachment
   TASK_ATTACHMENT_UPLOADED("comentario_adicionado_a_tarefa"),
   TASK_ATTACHMENT_DELETED("anexo_removido_da_tarefa");


   @JsonValue
   private final String type;


   // constructor
   private NotificationTitleEnum(String type) {
      this.type = type;
   }


   // getters
   public String getType() { return type; }


   // convert json for enum
   @JsonCreator
   public static NotificationTitleEnum fromType(String type) {
      for(NotificationTitleEnum title : NotificationTitleEnum.values()) {
         if(title.type.equalsIgnoreCase(type)) {
            return title;
         }
      }

      throw new IllegalArgumentException(
         "Título de notificação inválido: " + type
      );
   }


   // is valid
   public static boolean isValid(String type) {
      if(type == null) return false;
      
      for(NotificationTitleEnum title : NotificationTitleEnum.values()) {
         if(title.getType().equalsIgnoreCase(type)) {
            return true;
         }
      }
      
      return false;
   }

}