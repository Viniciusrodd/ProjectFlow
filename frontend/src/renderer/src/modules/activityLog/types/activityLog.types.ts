
// import enums
import { ActivityActionEnum } from "@activityLog/enums/activityActions.enum";

// uuid
export type UUID = string;


// activity log DTO
export interface ActivityLogDTO {
   organizationId: UUID | null;
   projectId: UUID | null;
   taskId: UUID | null;
   commentId: UUID | null;
   attachmentId: UUID | null;
   userId: UUID | null;
   action: ActivityActionEnum;
   description: string;
}


// activity log response
export interface ActivityLogResponseDTO {
   id: UUID | null;   
   organizationId: UUID | null;
   projectId: UUID | null;
   taskId: UUID | null;
   userId: UUID | null;
   action: ActivityActionEnum;
   description: string;
}