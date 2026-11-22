
// uuid
export type UUID = string;


// auth user
export interface iAuthUser {
   id: UUID | null;   
   name: string;
   email: string;
}