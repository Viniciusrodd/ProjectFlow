
// uuid
export type UUID = string;


// project image DTO
export interface ProjectImageDTO {
   id: UUID | null;
   projectId: UUID | null;
   fileName: string;
   mimeType: string;
   size: number;
   uploadDate: string;
}