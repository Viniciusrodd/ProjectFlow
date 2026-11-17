
// uuid
export type UUID = string;


// task checklist DTO
export interface TaskChecklistDTO {
   description: string;
   completed: boolean;
   position: number;
}


// task checklist response DTO
export interface TaskChecklistResponseDTO {
   id: UUID | null;
   taskId: UUID | null;
   description: string;
   completed: boolean;
   position: number;
}


// task checklist deleted DTO
export interface TaskChecklistDeletedDTO {
   id: UUID | null;
   taskId: UUID | null;
   description: string;
   completed: boolean;
   position: number;
   deletedAt: string;
}


// task checklist update DTO
export interface TaskChecklistUpdateDTO {
   description: string;
   position: number;
}