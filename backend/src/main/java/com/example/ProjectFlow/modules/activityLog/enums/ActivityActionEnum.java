
// packages
package com.example.ProjectFlow.modules.activityLog.enums;

// imports
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum ActivityActionEnum {
   
   // organização
   ORGANIZACAO_CRIADA("organizacao_criada"),
   ORGANIZACAO_ATUALIZADA("organizacao_atualizada"),
   ORGANIZACAO_DELETADA("organizacao_deletada"),
   LOGO_DA_ORGANIZACAO_ATUALIZADO("logo_da_organizacao_atualizado"),
   LOGO_DA_ORGANIZACAO_REMOVIDO("logo_da_organizacao_removido"),
   MEMBRO_ADICIONADO_A_ORGANIZACAO("membro_adicionado_a_organizacao"),
   PAPEL_DO_MEMBRO_DA_ORGANIZACAO_ATUALIZADA("papel_do_membro_da_organizacao_atualizada"),
   MEMBRO_REMOVIDO_DA_ORGANIZACAO("membro_removido_da_organizacao"),

   // projeto
   PROJETO_CRIADO("projeto_criado"),
   PROJETO_ATUALIZADO("projeto_atualizado"),
   PROJETO_DELETADO("projeto_deletado"),
   LOGO_DO_PROJETO_ATUALIZADO("logo_do_projeto_atualizado"),
   LOGO_DO_PROJETO_REMOVIDO("logo_do_projeto_removido"),
   MEMBRO_ADICIONADO_AO_PROJETO("membro_adicionado_ao_projeto"),
   PAPEL_DO_MEMBRO_DO_PROJETO_ATUALIZADA("papel_do_membro_do_projeto_atualizada"),
   MEMBRO_REMOVIDO_DO_PROJETO("membro_removido_do_projeto"),

   // tarefa
   TAREFA_CRIADA("tarefa_criada"),
   POSICAO_DA_TAREFA_ATUALIZADA("posicao_da_tarefa_atualizada"),
   TAREFA_ATUALIZADA("tarefa_atualizada"),
   TAREFA_CONCLUIDA("tarefa_concluida"),
   TAREFA_DELETADA("tarefa_deletada"),

   // etiqueta da tarefa
   ETIQUETA_DA_TAREFA_ADICIONADA("etiqueta_da_tarefa_adicionada"),
   ETIQUETA_DA_TAREFA_REMOVIDA("etiqueta_da_tarefa_removida"),

   // checklist da tarefa
   CHECKLIST_DA_TAREFA_CRIADO("checklist_da_tarefa_criado"),
   CHECKLIST_DA_TAREFA_ATUALIZADO("checklist_da_tarefa_atualizado"),
   CHECKLIST_DA_TAREFA_REMOVIDO("checklist_da_tarefa_removido"),

   // comentário
   COMENTARIO_DA_TAREFA_CRIADO("comentario_da_tarefa_criado"),
   COMENTARIO_DA_TAREFA_ATUALIZADO("comentario_da_tarefa_atualizado"),
   COMENTARIO_DA_TAREFA_REMOVIDO("comentario_da_tarefa_removido"),

   // anexo
   ANEXO_DA_TAREFA_ADICIONADO("anexo_da_tarefa_adicionado"),
   ANEXO_DA_TAREFA_REMOVIDO("anexo_da_tarefa_removido");


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