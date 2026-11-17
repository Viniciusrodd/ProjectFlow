
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