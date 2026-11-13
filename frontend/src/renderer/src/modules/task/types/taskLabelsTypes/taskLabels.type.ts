
// import types
import { TasksResponseDTO } from "@task/types/taskTypes/task.type";
import { LabelsResponseDTO } from "@labels/types/labels.types";

// uuid
export type UUID = string;


// tasks by label response DTO
export interface TasksByLabelResponseDTO {
   id: UUID | null;   
   task: TasksResponseDTO;
}


// task labels response DTO
export interface TaskLabelsResponseDTO {
   id: UUID | null;   
   taskId: UUID | null;   
   labelId: UUID | null;   
   createdAt: string;
}


// task labels deleted DTO
export interface TaskLabelsDeletedDTO {
   id: UUID | null;   
   taskId: UUID | null;   
   labelId: UUID | null;   
   createdAt: string;
   deletedAt: string;
}


// labels by task response DTO
export interface LabelsByTaskResponseDTO {
   id: UUID | null;   
   label: LabelsResponseDTO
}