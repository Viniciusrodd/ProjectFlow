
// import enums
import { PriorityEnum } from "@task/enums/priority.enum";

// uuid
export type UUID = string;


// tasks DTO
export interface TasksDTO {
   projectId: UUID | null;   
   columnId: UUID | null;   
   ownerId: UUID | null;   
   title: string;
   description: string;
   priority: string;
   dueDate: string;
}


// tasks reponse DTO
export interface TasksResponseDTO {
   id: UUID | null;
   projectId: UUID | null;   
   columnId: UUID | null;   
   ownerId: UUID | null;   
   title: string;
   description: string;
   priority: PriorityEnum;
   due_date: string;
}


// tasks complete reponse DTO
export interface TasksCompleteResponseDTO {
   id: UUID | null;
   projectId: UUID | null;   
   columnId: UUID | null;   
   ownerId: UUID | null;   
   title: string;
   description: string;
   priority: PriorityEnum;
   due_date: string;
   completedAt: string;
}


// tasks deleted DTO
export interface TasksDeletedDTO {
   id: UUID | null;
   projectId: UUID | null;   
   columnId: UUID | null;   
   ownerId: UUID | null;   
   title: string;
   description: string;
   priority: PriorityEnum;
   due_date: string;
   completedAt: string;
   deletedAt: string;
}


// tasks update DTO
export interface TasksUpdateDTO {
   title: string;
   description: string;
   priority: PriorityEnum;
   due_date: string;
}