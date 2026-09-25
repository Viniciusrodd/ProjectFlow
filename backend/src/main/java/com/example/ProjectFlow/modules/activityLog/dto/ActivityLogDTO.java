
// packages
package com.example.ProjectFlow.modules.activityLog.dto;

// imports
import java.util.UUID;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


public record ActivityLogDTO (

   UUID organizationId,
   UUID projectId,
   UUID taskId,
   UUID userId,
   ActivityActionEnum action,
   String description

) {}