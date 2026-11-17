
// uuid
export type UUID = string;


// attachment response DTO
export interface AttachmentResponseDTO {
   id: string;
   taskId: UUID | null;
   uploadedBy: UUID | null;
   fileName: string;
   mimeType: string;
   size: number;
   uploadDate: string; 
}