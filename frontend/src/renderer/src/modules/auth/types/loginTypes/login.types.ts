
// uuid
export type UUID = string;


// login DTO
export interface LoginDTO {
   email: string;
   password: string;
}


// login response DTO
export interface LoginResponseDTO {
   id: UUID | null;   
   name: string;
   email: string;
   token: string;
}