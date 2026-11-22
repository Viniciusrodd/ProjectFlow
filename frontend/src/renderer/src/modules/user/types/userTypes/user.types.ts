
// import interfaces
import { iApiResponse } from "@shared/types/api/api.types";


// uuid
export type UUID = string;


// user DTO
export interface UserDTO {
   id: UUID | null;
   email: string;
   name: string;
   password: string;
}


// user profile DTO
export interface UserProfileDTO {
   id: UUID | null;
   email: string;
   name: string;
   profileImageId: string;
}


// user update DTO
export interface UserUpdateDTO {
   email: string;
   name: string;
   password: string;
}


// user deleted DTO
export interface UserDeletedDTO {
   id: UUID | null;
   email: string;
   name: string;
   deletedAt: string;
}


// user updated DTO
export interface UserDeletedDTO {
   email: string;
   name: string;
   password: string;
}


// user methods
export interface iUserMethods {

   getAllUsers(): Promise<iApiResponse<UserProfileDTO[]>>
   getById(id: UUID | null): Promise<iApiResponse<UserProfileDTO>>
   updateUser(
      userId: UUID | null, 
      data: UserUpdateDTO
   ): Promise<iApiResponse<UserProfileDTO>>
   deleteUser(userId: UUID | null): Promise<iApiResponse<UserDeletedDTO>>

}