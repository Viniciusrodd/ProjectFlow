
// packages
package com.example.ProjectFlow.modules.notification.dto;

// imports
import java.util.UUID;


public record NotificationResponseDTO (

   String id,
   UUID userId,
   String title,
   String message,
   boolean read

) {}