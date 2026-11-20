
// import types
import { iApiResponse } from "@shared/types/api/api.types";


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


// organization methods
export interface iOrganizationMethods {
   createOrganization(data: OrganizationDTO): Promise<iApiResponse<OrganizationResponseDTO>>
   
   getAllOrganizations(): Promise<iApiResponse<OrganizationResponseDTO[]>>
   
   getById(id: UUID | null): Promise<iApiResponse<OrganizationResponseDTO>>
   
   getByOwnerId(ownerId: UUID | null): Promise<iApiResponse<OrganizationResponseDTO[]>>
   
   updateOrganization(
      id: UUID | null,
      userId: UUID | null,
      data: OrganizationUpdateDTO
   ): Promise<iApiResponse<OrganizationResponseDTO>>
   
   deleteOrganization(
      id: UUID | null,
      userId: UUID | null
   ): Promise<iApiResponse<OrganizationDeletedDTO>>
}