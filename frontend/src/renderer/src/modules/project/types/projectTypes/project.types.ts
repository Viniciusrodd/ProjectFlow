
// import enums
import { StatusEnum } from "@project/enums/status.enum";

// uuid
export type UUID = string;


// project DTO
export interface ProjectDTO {
   organizationId: UUID | null;
   ownerId: UUID | null;
   name: string;
   description: string;
}


// project response DTO
export interface ProjectResponseDTO {
   id: UUID | null;
   organizationId: UUID | null;
   ownerId: UUID | null;
   name: string;
   description: string;
   status: StatusEnum;
}


// project deleted DTO
export interface ProjectDeletedDTO {
   id: UUID | null;
   organizationId: UUID | null;
   ownerId: UUID | null;
   name: string;
   description: string;
   status: StatusEnum;
   deletedAt: string;
}


// project update DTO
export interface ProjectUpdateDTO {
   name: string;
   description: string;
   status: StatusEnum;
}