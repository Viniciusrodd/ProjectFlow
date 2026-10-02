
// packages
package com.example.ProjectFlow.modules.activityLog.enums;

// imports
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum ActivityActionEnum {
   
   // organization
   ORGANIZATION_CREATED("organizacao_criada"),
   ORGANIZATION_UPDATED("organizacao_atualizada"),
   ORGANIZATION_DELETED("organizacao_excluida"),
   ORGANIZATION_LOGO_UPDATED("logo_da_organizacao_atualizado"),
   ORGANIZATION_LOGO_REMOVED("logo_da_organizacao_removido"),
   ORGANIZATION_MEMBER_ADDED("membro_adicionado_a_organizacao"),
   ORGANIZATION_MEMBER_ROLE_UPDATED("permissao_do_membro_da_organizacao_atualizada"),
   ORGANIZATION_MEMBER_REMOVED("membro_removido_da_organizacao"),


   // project
   PROJECT_CREATED("projeto_criado"),
   PROJECT_UPDATED("projeto_atualizado"),
   PROJECT_DELETED("projeto_excluido"),
   PROJECT_LOGO_UPDATED("logo_do_projeto_atualizado"),
   PROJECT_LOGO_REMOVED("logo_do_projeto_removido"),
   PROJECT_MEMBER_ADDED("membro_adicionado_ao_projeto"),
   PROJECT_MEMBER_ROLE_UPDATED("permissao_do_membro_do_projeto_atualizada"),
   PROJECT_MEMBER_REMOVED("membro_removido_do_projeto"),


   // task
   TASK_CREATED("tarefa_criada"),
   TASK_POSITION_UPDATED("posicao_da_tarefa_atualizada"),
   TASK_UPDATED("tarefa_atualizada"),
   TASK_COMPLETED("tarefa_concluida"),
   TASK_DELETED("tarefa_excluida"),


   // task label
   TASK_LABEL_ADDED("etiqueta_da_tarefa_adicionada"),
   TASK_LABEL_REMOVED("etiqueta_da_tarefa_removida"),


   // task checklist
   TASK_CHECKLIST_CREATED("checklist_da_tarefa_criado"),
   TASK_CHECKLIST_UPDATED("checklist_da_tarefa_atualizado"),
   TASK_CHECKLIST_REMOVED("checklist_da_tarefa_removido"),


   // comment
   COMMENT_CREATED("comentario_da_tarefa_criado"),
   COMMENT_UPDATED("comentario_da_tarefa_atualizado"),
   COMMENT_DELETED("comentario_da_tarefa_excluido"),


   // attachment
   ATTACHMENT_UPLOADED("anexo_da_tarefa_adicionado"),
   ATTACHMENT_DELETED("anexo_da_tarefa_excluido");


   @JsonValue
   private final String type;


   // constructor
   private ActivityActionEnum(String type) {
      this.type = type;
   }


   // getters
   public String getType() { return type; }


   // convert json for enum
   @JsonCreator
   public static ActivityActionEnum fromType(String type) {
      for(ActivityActionEnum action : ActivityActionEnum.values()) {
         if(action.type.equalsIgnoreCase(type)) {
            return action;
         }
      }

      throw new IllegalArgumentException(
         "Ação de atividade inválida: " + type
      );
   }


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