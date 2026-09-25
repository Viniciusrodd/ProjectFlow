
// packages
package com.example.ProjectFlow.modules.activityLog.dto;

// imports
import java.util.UUID;

// import enums
import com.example.ProjectFlow.modules.activityLog.enums.ActivityActionEnum;


public record ActivityLogResponseDTO (

   String id,
   UUID organizationId,
   UUID projectId,
   UUID taskId,
   UUID userId,
   ActivityActionEnum action,
   String description
   
) {}