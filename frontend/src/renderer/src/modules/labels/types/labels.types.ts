
// uuid
export type UUID = string;


// labels DTO
export interface LabelsDTO {
   projectId: UUID | null;
   name: string;
   color: string;
}


// labels deleted DTO
export interface LabelsDeletedDTO {
   id: UUID | null;
   projectId: UUID | null;
   name: string;
   color: string;
   deletedAt: string;
}


// labels response DTO
export interface LabelsResponseDTO {
   id: UUID | null;
   projectId: UUID | null;
   name: string;
   color: string;
}


// labels udpate DTO
export interface LabelsUpdateDTO {
   name: string;
   color: string;
}