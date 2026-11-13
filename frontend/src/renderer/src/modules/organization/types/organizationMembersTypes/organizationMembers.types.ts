
// import types
import { UserProfileDTO } from "@user/types/userTypes/user.types";

// import enums
import { RoleEnum } from "@organization/enums/role.enum"; 

// uuid
export type UUID = string;


// organization members DTO
export interface OrganizationMembersDTO {
   organizationId: UUID | null;
   userId: UUID | null;
   role: RoleEnum;
}


// organization members response DTO
export interface OrganizationMembersResponseDTO {
   id: UUID | null;
   organizationId: UUID | null;
   userId: UUID | null;
   role: RoleEnum;
   joinedAt: string;
}


// member by organization response DTO
export interface MemberByOrganizationResponseDTO {
   id: UUID | null;
   user: UserProfileDTO;
   role: RoleEnum;
   joinedAt: string;
}


// organization members deleted DTO
export interface OrganizationMembersDeletedDTO {
   organizationId: UUID | null;
   userId: UUID | null;
   role: RoleEnum;
   joinedAt: string;
   deletedAt: string;
}