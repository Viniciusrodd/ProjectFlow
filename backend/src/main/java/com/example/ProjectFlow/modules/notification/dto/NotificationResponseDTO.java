
// packages
package com.example.ProjectFlow.modules.notification.dto;

// imports
import java.util.UUID;

// import enums
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;


public record NotificationResponseDTO (

   String id,
   UUID userId,
   NotificationTitleEnum title,
   String message,
   boolean read

) {}