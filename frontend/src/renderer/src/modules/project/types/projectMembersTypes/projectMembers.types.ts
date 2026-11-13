
// import types
import { UserProfileDTO } from "@user/types/userTypes/user.types";

// import enums
import { RoleEnum } from "@project/enums/role.enum";

// uuid
export type UUID = string;


// project members DTO
export interface ProjectMembersDTO {
   projectId: UUID | null;
   userId: UUID | null;
   role: string;
}


// project members response DTO
export interface ProjectMembersResponseDTO {
   projectId: UUID | null;
   userId: UUID | null;
   role: RoleEnum;
   joinedAt: string;
}


// member by project response DTO
export interface MemberByProjectResponseDTO {
   id: UUID | null;
   user: UserProfileDTO;
   role: RoleEnum;
   joinedAt: string;
}


// project members deleted DTO
export interface ProjectMembersDeletedDTO {
   projectId: UUID | null;
   userId: UUID | null;
   role: RoleEnum;
   joinedAt: string;
   deletedAt: string;
}