
// packages
package com.example.ProjectFlow.modules.notification.dto;

// import enums
import com.example.ProjectFlow.modules.notification.enums.NotificationTitleEnum;


public record NotificationDTO (

   NotificationTitleEnum title,
   String message

) {}