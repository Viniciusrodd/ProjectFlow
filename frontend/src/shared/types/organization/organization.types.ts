
// uuid
export type UUID = string;


// organization DTO
export interface OrganizationDTO {
   ownerId: UUID | null;
   name: string;
   description: string;
}


// organization deleted DTO
export interface OrganizationDeletedDTO {
   id: UUID | null;
   ownerId: UUID | null;
   name: string;
   description: string;
   deletedAt: string;
}


// organization response DTO
export interface OrganizationResponseDTO {
   id: UUID | null;
   ownerId: UUID | null;
   name: string;
   description: string;
}


// organization update DTO
export interface OrganizationUpdateDTO {
   name: string;
   description: string;
}