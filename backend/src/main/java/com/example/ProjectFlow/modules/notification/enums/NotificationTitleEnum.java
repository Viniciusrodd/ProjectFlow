
// packages
package com.example.ProjectFlow.modules.notification.enums;

// imports
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum NotificationTitleEnum {
 
   // organização
   MEMBRO_ADICIONADO_A_ORGANIZACAO("membro_adicionado_a_organizacao"),
   PAPEL_DO_MEMBRO_DA_ORGANIZACAO_ATUALIZADA("papel_do_membro_da_organizacao_atualizada"),
   MEMBRO_REMOVIDO_DA_ORGANIZACAO("membro_removido_da_organizacao"),

   // projeto
   MEMBRO_ADICIONADO_AO_PROJETO("membro_adicionado_ao_projeto"),
   PAPEL_DO_MEMBRO_DO_PROJETO_ATUALIZADA("papel_do_membro_do_projeto_atualizada"),
   MEMBRO_REMOVIDO_DO_PROJETO("membro_removido_do_projeto"),

   // tarefa
   TAREFA_ATRIBUIDA("tarefa_atribuida"),
   STATUS_DA_TAREFA_ATUALIZADO("status_da_tarefa_atualizado"),
   TAREFA_CONCLUIDA("tarefa_concluida"),
   TAREFA_EXCLUIDA("tarefa_excluida"),

   // etiqueta da tarefa
   ETIQUETA_DA_TAREFA_ADICIONADA("etiqueta_da_tarefa_adicionada"),
   ETIQUETA_DA_TAREFA_REMOVIDA("etiqueta_da_tarefa_removida"),

   // checklist da tarefa
   CHECKLIST_DA_TAREFA_ATUALIZADO("checklist_da_tarefa_atualizado"),

   // comentário
   COMENTARIO_ADICIONADO_A_TAREFA("comentario_adicionado_a_tarefa"),

   // anexo
   ANEXO_ADICIONADO_A_TAREFA("anexo_adicionado_a_tarefa"),
   ANEXO_REMOVIDO_DA_TAREFA("anexo_removido_da_tarefa");


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