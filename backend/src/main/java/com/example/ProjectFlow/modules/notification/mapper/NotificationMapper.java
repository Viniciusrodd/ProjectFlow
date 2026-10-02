
// packages
package com.example.ProjectFlow.modules.notification.mapper;

// imports
import org.springframework.stereotype.Component;

// import document
import com.example.ProjectFlow.modules.notification.document.NotificationDocument;

// import DTOs
import com.example.ProjectFlow.modules.notification.dto.NotificationResponseDTO;


@Component 
public class NotificationMapper {

   // from NotificationDocument to NotificationResponseDTO
   public NotificationResponseDTO toNotificationResponseDTO(NotificationDocument document) {
      return new NotificationResponseDTO(
         document.getId(),
         document.getUserId(),
         document.getTitle(),
         document.getMessage(),
         document.getRead()
      );
   }

}