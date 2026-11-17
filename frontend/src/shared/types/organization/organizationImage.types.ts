
// uuid
export type UUID = string;


// organization image response DTO
export interface OrganizationImageResponseDTO {
   id: UUID | null;
   organizationId: UUID | null;
   fileName: string;
   mimeType: string;
   size: number;
   uploadDate: string;
}