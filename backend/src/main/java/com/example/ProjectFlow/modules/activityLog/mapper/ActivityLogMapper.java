
// packages
package com.example.ProjectFlow.modules.activityLog.mapper;

// imports
import org.springframework.stereotype.Component;

// import document
import com.example.ProjectFlow.modules.activityLog.document.ActivityLogDocument;

// import DTOs
import com.example.ProjectFlow.modules.activityLog.dto.ActivityLogResponseDTO;


@Component 
public class ActivityLogMapper {
   
   // from ActivityLogDocument to ActivityLogResponseDTO
   public ActivityLogResponseDTO toActivityLogResponseDTO(ActivityLogDocument document) {
      return new ActivityLogResponseDTO(
         document.getId(),
         document.getOrganizationId(),
         document.getProjectId(),
         document.getTaskId(),
         document.getUserId(),
         document.getAction(),
         document.getDescription()
      );
   }

}