
// uuid
export type UUID = string;


// board DTO
export interface BoardDTO {
   projectId: UUID | null;
   name: string;
}


// board deleted DTO
export interface BoardDeletedDTO {
   id: UUID | null;
   projectId: UUID | null;
   name: string;
   deletedAt: string;
}


// board response DTO
export interface BoardResponseDTO {
   id: UUID | null;
   projectId: UUID | null;
   name: string;
}