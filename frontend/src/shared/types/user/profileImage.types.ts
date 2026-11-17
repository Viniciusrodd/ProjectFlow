
// uuid
export type UUID = string;


// profile image response DTO
export interface ProfileImageResponseDTO {
   id: UUID | null;
   userId: UUID | null;
   fileName: string;
   mimeType: string;
   size: number;
   uploadDate: string;
}