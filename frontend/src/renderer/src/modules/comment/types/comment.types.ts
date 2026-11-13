
// uuid
export type UUID = string;


// comment DTO
export interface CommentDTO {
   taskId: UUID | null;
   authorId: UUID | null;
   content: string;
}


// comment delete DTO
export interface CommentDeleteDTO {
   id: UUID | null;
   taskId: UUID | null;
   authorId: UUID | null;
   content: string;
   deletedAt: string;
}


// comment response DTO
export interface CommentResponseDTO {
   id: UUID | null;
   taskId: UUID | null;
   authorId: UUID | null;
   content: string;
}